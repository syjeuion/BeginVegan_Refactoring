package com.beginvegan.presentation.view.mypage.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMypageMyRestaurantBinding
import com.beginvegan.presentation.network.NetworkResult
import com.beginvegan.presentation.util.DefaultDialog
import com.beginvegan.presentation.util.LOADING
import com.beginvegan.presentation.util.LoadingDialog
import com.beginvegan.presentation.util.setContentToolbar
import com.beginvegan.presentation.view.mypage.adapter.MyRestaurantRvAdapter
import com.beginvegan.presentation.view.mypage.viewModel.MyRestaurantViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MypageMyRestaurantFragment :
    BaseFragment<FragmentMypageMyRestaurantBinding>(FragmentMypageMyRestaurantBinding::inflate) {
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val myRestaurantViewModel: MyRestaurantViewModel by viewModels()
    //로딩
    private lateinit var loadingDialog: LoadingDialog

    private val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    private lateinit var currentLocation: LatLng

    private lateinit var myRestaurantRvAdapter: MyRestaurantRvAdapter

    override fun init() {
        loadingDialog = LoadingDialog.newInstance()

        checkAndRequestPermissions()
        setToolbar()
        setBackUp()
        setFabButton()
    }

    private fun setToolbar(){
        setContentToolbar(
            requireContext(),
            binding.includedToolbar,
            getString(R.string.mypage_my_restaurant)
        )
    }

    private var isLocationUpdateRequested: Boolean = false

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.let { result ->
                for (location in result.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // 현재 위치 저장
                    currentLocation = LatLng.from(latitude, longitude)
                    setRvAdapter()
                    break // 첫 번째 위치만 사용하고 루프 종료
                }
            }
        }
    }

    private fun setRvAdapter() {
        myRestaurantViewModel.reSetVieModel()

        myRestaurantRvAdapter = MyRestaurantRvAdapter(
            requireContext(),
            onItemClick
        )
        binding.rvMyRestaurant.adapter = myRestaurantRvAdapter
        binding.rvMyRestaurant.layoutManager = LinearLayoutManager(this.context)

        setListener()
        getMyRestaurantList()
    }
    private val onItemClick = {id:Int ->
        Timber.d("onItemClick: id:$id")
    }

    private fun getMyRestaurantList() {
        viewLifecycleOwner.lifecycleScope.launch {
            myRestaurantViewModel.currentPage.collectLatest {
                myRestaurantViewModel.getMyRestaurant(
                    currentLocation.latitude.toString(),
                    currentLocation.longitude.toString()
                )
            }
        }
    }

    private fun setListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            myRestaurantViewModel.myRestaurantState.collectLatest { state ->
                when (state) {
                    is NetworkResult.Loading -> {
                        if(!loadingDialog.isAdded) loadingDialog.show(childFragmentManager, LOADING)
                    }
                    is NetworkResult.Success -> {
                        dismiss()
                        myRestaurantRvAdapter.submitList(state.data)
                    }
                    is NetworkResult.Error -> dismiss()
                    is NetworkResult.Empty -> {
                        dismiss()
                        myRestaurantRvAdapter.submitList(emptyList())
                    }
                }
            }
        }

        myRestaurantViewModel.isRestaurantEmpty.observe(this) {
            setEmptyState(it)
        }
    }

    private fun dismiss() {
        if (loadingDialog.isAdded) loadingDialog.dismiss()
    }

    private fun checkAndRequestPermissions() {
        when {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                logMessage("checkAndRequestPermissions 정확한 위치 권한 승인")
                getLocationPermission()
            }

            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                logMessage("checkAndRequestPermissions 대략적인 위치 권한 승인")
                getLocationPermission()
            }

            else -> {
                logMessage("checkAndRequestPermissions 위치 권한 없음")
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        ACCESS_COARSE_LOCATION
                    )
                ) {
                    logMessage(
                        "shouldShowRequestPermissionRationale = true"
                    )
                    showPermissionRationaleDialog()
                } else {
                    logMessage(
                        "shouldShowRequestPermissionRationale = false"
                    )
                    locationPermissionLauncher.launch(permissions)
                }

            }
        }
    }

    // 권한 재요청 다이얼로그
    private fun showPermissionRationaleDialog() {
        var isRetry = false
        DefaultDialog.Builder()
            .setTitle("권한 재요청 안내")
            .setBody(
                "해당 권한을 거부할 경우, 다음 기능의 사용이 불가능해요." +
                        "\n · Map 기능 전체 "
            )
            .setPositiveButton("권한재요청") {
                isRetry = true
                locationPermissionLauncher.launch(permissions)
            }.setNegativeButton("닫기") {
                logMessage("닫기")
            }
            .setOnDismissListener {
                if (!isRetry) {
                    showPermissionDeniedDialog()
                }
            }
            .show(childFragmentManager, "showPermissionRationaleDialog")
    }

    // 권한 허용 안함 다이얼로그
    private fun showPermissionDeniedDialog() {
        DefaultDialog.Builder()
            .setTitle("기능 사용 불가 안내")
            .setBody(
                "위치 정보에 대한 권한 사용을 거부하셨어요.\n" +
                        "\n" +
                        "기능 사용을 원하실 경우 [휴대폰 설정 > 애플리케이션 관리자]에서 해당 앱의 권한을 허용해 주세요."
            )
            .setPositiveButton("확인") {
                logMessage("showPermissionDeniedDialog 확인")
            }.show(childFragmentManager, "showPermissionDeniedDialog")
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없는 경우 처리
            logMessage("Location permission Not Granted")
            return
        } else {
            logMessage("Location permission Granted")
        }

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    currentLocation = LatLng.from(latitude, longitude)
                    setRvAdapter()
                } ?: run {
                    requestLocationUpdates()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                logMessage("Failed to get location")
            }
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .apply {
                setMinUpdateDistanceMeters(10f) // 위치 업데이트 간의 최소 거리 (10미터)
                setGranularity(Granularity.GRANULARITY_FINE)
                setWaitForAccurateLocation(true)
            }.build()

        try {
            // 위치 업데이트 요청
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            isLocationUpdateRequested = true
        } catch (e: SecurityException) {
            e.printStackTrace()
            // 위치 권한이 없는 경우 처리
            logMessage("Location permission not granted")
        }
    }

    private fun showFineLocationDialog() {
        DefaultDialog.Builder()
            .setTitle("정확한 위치 권한 요청 안내")
            .setBody(
                "Map 메뉴는 '정확한 위치' 권한으로만 사용 가능합니다.\n" +
                        "'정확한 위치' 사용 권한을 허용해 주세요."
            )
            .setPositiveButton("설정") {
                locationPermissionLauncher.launch(permissions)
            }.setNegativeButton("닫기") {
                showPermissionDeniedDialog()
                logMessage("showFineLocationDialog 닫기")
            }.show(childFragmentManager, "showFineLocationDialog")
    }


    private fun setBackUp() {
        binding.includedToolbar.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setFabButton() {
        binding.ibFab.setOnClickListener {
            binding.rvMyRestaurant.smoothScrollToPosition(0)
        }
    }

    private fun setEmptyState(emptyState: Boolean) {
        binding.llEmptyArea.isVisible = emptyState
        binding.btnMoveToMap.setOnClickListener {
            mainNavigationHandler.navigateMyRestaurantToMap()
        }
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val isFineLocation = isGranted[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val isCoarseLocation = isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            when {
                isFineLocation && isCoarseLocation -> {
                    // 정확한 위치 권한 승인
                    logMessage("locationPermissionLauncher Fine Location, Coarse Location Granted 정확한 위치 권한 승인")
                    getLocationPermission()
                }

                !isFineLocation && isCoarseLocation -> {
                    // 대략적인 위치 권한 승인
                    logMessage("locationPermissionLauncher Only Coarse Location Granted 대략적인 위치 권한 승인")
                    getLocationPermission()
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            ACCESS_FINE_LOCATION
                        )
                    ) {
                        logMessage("locationPermissionLauncher Fine Location 거부 경험 있음")
                        showPermissionDeniedDialog()
                    } else {
                        logMessage("locationPermissionLauncher Fine Location 거부 경험 없음")
                        showFineLocationDialog()
                    }

                }

                else -> {
                    // 위치 권한 승인하지 않음
                    logMessage("locationPermissionLauncher Permission Denied 위치 권한 거부")
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            ACCESS_COARSE_LOCATION
                        )
                    ) {
                        logMessage("locationPermissionLauncher 위치 권한 거부 경험 없음")
                        showPermissionRationaleDialog()
                    } else {
                        logMessage("locationPermissionLauncher 위치 권한 거부 경험 있음")
                        showPermissionDeniedDialog()
                    }
                }
            }
        }

    companion object {
        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }

    override fun onStop() {
        super.onStop()
        if(loadingDialog.isAdded) loadingDialog.onDestroy()
    }
}