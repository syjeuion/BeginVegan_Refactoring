package com.beginvegan.presentation.view.home.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.presentation.R
import com.beginvegan.presentation.adapter.home.HomeRestaurantRVAdapter
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMainHomeBinding
import com.beginvegan.presentation.util.BookmarkController
import com.beginvegan.presentation.util.DrawerController
import com.beginvegan.presentation.util.DefaultDialog
import com.beginvegan.presentation.util.UserLevelLists
import com.beginvegan.presentation.view.main.viewModel.MainViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.beginvegan.presentation.view.mypage.view.MypagePushAlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentMainHomeBinding>(FragmentMainHomeBinding::inflate) {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_main_graph)

    @Inject
    lateinit var drawerController: DrawerController
    @Inject
    lateinit var bookmarkController: BookmarkController
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private var tipsNowTab = "MAGAZINE"

    private var typeface: Typeface? = null

    private val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager

    private val REQUEST_NOTIFICATION_PERMISSION = 2

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val isFineLocation = isGranted[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val isCoarseLocation = isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            when {
                isFineLocation && isCoarseLocation -> {
                    // FineLoaction 승인 시, CoarseLoaction 자동 승인
                    // 정확한 위치 권한 승인
                    logMessage("locationPermissionLauncher Fine Location, Coarse Location Granted 정확한 위치 권한 승인")
                    getLocation()
                    getFineLocation()
                }

                !isFineLocation && isCoarseLocation -> {
                    // 대략적인 위치 권한 승인
                    logMessage("locationPermissionLauncher Only Coarse Location Granted 대략적인 위치 권한 승인")
                    getLocation()
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


    override fun init() {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
        mainViewModel.getRecommendRestaurant()
        mainViewModel.getUserInfo()

        setUserInfo()
        setTipsTab()
        setOpenDrawer()
        setBeganTest()
        checkAndRequestPermissions()
        setRecommendRestaurant()
        setMoreButton()
    }

    private fun setMoreButton() {
        binding.btnRecommendMore.setOnClickListener {
            mainNavigationHandler.navigateToMap()
        }
    }

    private fun setRecommendRestaurant() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.recommendRestaurantList.collectLatest { restaurantList ->
                if (restaurantList.isNotEmpty()) {
                    logMessage("viewLifecycleOwner collect restaurantList $restaurantList")
                    binding.tvRestaurantNameFirst.text = restaurantList[0].name
                    binding.tvRestaurantNameSecond.text = restaurantList[1].name
                    binding.tvRestaurantNameThird.text = restaurantList[2].name

                    Glide.with(requireContext())
                        .load(restaurantList[0].thumbnail)
                        .error(R.drawable.default_cafe_image)
                        .into(binding.ivItemRestaurantImageFirst)
                    Glide.with(requireContext())
                        .load(restaurantList[1].thumbnail)
                        .error(R.drawable.default_cafe_image)
                        .into(binding.ivItemRestaurantImageSecond)
                    Glide.with(requireContext())
                        .load(restaurantList[2].thumbnail)
                        .error(R.drawable.default_cafe_image)
                        .into(binding.ivItemRestaurantImageThird)

                    binding.ivItemRestaurantHeartFirst.isChecked = restaurantList[0].bookmark
                    binding.ivItemRestaurantHeartSecond.isChecked = restaurantList[1].bookmark
                    binding.ivItemRestaurantHeartThird.isChecked = restaurantList[2].bookmark

                }
            }
        }
        binding.ivItemRestaurantImageFirst.setOnClickListener {
            showToast(mainViewModel.recommendRestaurantList.value[0].name)
            val action = HomeFragmentDirections.actionMainHomeFragmentToRestaurantDetailFragment(
                mainViewModel.recommendRestaurantList.value[0].id,
                mainViewModel.recommendRestaurantList.value[0].latitude,
                mainViewModel.recommendRestaurantList.value[0].longitude,
                mainViewModel.recommendRestaurantList.value[0].thumbnail
            )
            findNavController().navigate(action)
        }
        binding.ivItemRestaurantImageSecond.setOnClickListener {
            showToast(mainViewModel.recommendRestaurantList.value[1].name)
            val action = HomeFragmentDirections.actionMainHomeFragmentToRestaurantDetailFragment(
                mainViewModel.recommendRestaurantList.value[1].id,
                mainViewModel.recommendRestaurantList.value[1].latitude,
                mainViewModel.recommendRestaurantList.value[1].longitude,
                mainViewModel.recommendRestaurantList.value[1].thumbnail
            )
            findNavController().navigate(action)
        }
        binding.ivItemRestaurantImageThird.setOnClickListener {
            showToast(mainViewModel.recommendRestaurantList.value[2].name)
            val action = HomeFragmentDirections.actionMainHomeFragmentToRestaurantDetailFragment(
                mainViewModel.recommendRestaurantList.value[2].id,
                mainViewModel.recommendRestaurantList.value[2].latitude,
                mainViewModel.recommendRestaurantList.value[2].longitude,
                mainViewModel.recommendRestaurantList.value[2].thumbnail
            )
            findNavController().navigate(action)
        }

        binding.ivItemRestaurantHeartFirst.setOnCheckedChangeListener { _, isChecked ->
            changeBookmark(isChecked, mainViewModel.recommendRestaurantList.value[0].id.toInt())
        }
        binding.ivItemRestaurantHeartSecond.setOnCheckedChangeListener { _, isChecked ->
            changeBookmark(isChecked, mainViewModel.recommendRestaurantList.value[1].id.toInt())
        }
        binding.ivItemRestaurantHeartThird.setOnCheckedChangeListener { _, isChecked ->
            changeBookmark(isChecked, mainViewModel.recommendRestaurantList.value[2].id.toInt())
        }
        requestNotificationPermission()

//        mainViewModel.postFcmPush()
    }

    private fun changeBookmark(isBookmarked: Boolean, restaurantId: Int) {
        lifecycleScope.launch {
            if (isBookmarked) {
                bookmarkController.postBookmark(restaurantId, "RESTAURANT")
                val snackbar = Snackbar.make(
                    binding.clMain,
                    getString(R.string.toast_scrap_done),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.toast_scrap_action)) {
                        mainNavigationHandler.navigateMypageToMyRestaurant()
                    }
                    .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .setTypeface(typeface)
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
                    .setTypeface(typeface)
                snackbar.show()
            } else {
                bookmarkController.deleteBookmark(restaurantId, "RESTAURANT")
                val snackbar = Snackbar.make(
                    binding.clMain,
                    getString(R.string.toast_scrap_undo),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.toast_scrap_action)) {
                        mainNavigationHandler.navigateMypageToMyRestaurant()
                    }
                    .setActionTextColor(resources.getColor(R.color.color_primary_variant_02))
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .setTypeface(typeface)
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
                    .setTypeface(typeface)
                snackbar.show()
            }
        }
    }

    private fun setUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.nickName.collectLatest { userName ->
                logMessage("viewModel userName ${userName}")
                val headlineText = getString(R.string.home_user_name, userName)
                val headlineSpannable = SpannableString(headlineText)

                val userNameColor1 =
                    ContextCompat.getColor(requireContext(), R.color.colorUserName2)
                val userNameStartIndex1 = headlineText.indexOf(userName)
                val userNameEndIndex1 = userNameStartIndex1 + userName.length

                headlineSpannable.setSpan(
                    ForegroundColorSpan(userNameColor1),
                    userNameStartIndex1,
                    userNameEndIndex1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.homeHeadlineUserName.text = headlineSpannable

                val recommendTitleText = getString(R.string.home_restaurant_title, userName)
                val recommendSpannable = SpannableString(recommendTitleText)

                val userNameColor2 =
                    ContextCompat.getColor(requireContext(), R.color.colorUserName2)
                val userNameStartIndex2 = recommendTitleText.indexOf(userName)
                val userNameEndIndex2 = userNameStartIndex2 + userName.length

                recommendSpannable.setSpan(
                    ForegroundColorSpan(userNameColor2),
                    userNameStartIndex2,
                    userNameEndIndex2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.tvRecommendTitle.text = recommendSpannable
            }


        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.userLevel.collect { userLevel ->
                if (userLevel.isNotBlank()) {
                    logMessage("viewModel userLevel ${userLevel}")
                    val userLevelEng = resources.getStringArray(R.array.user_levels_eng)
                    val userLevelLists = UserLevelLists(requireContext())
                    val levelIcons = userLevelLists.userLevelIcons
                    val userLevelIndex = userLevelEng.indexOf(userLevel)
                    logMessage("viewModel userLevelIndex ${userLevelIndex}")
                    Glide.with(requireContext())
                        .load(levelIcons[userLevelIndex])
                        .into(binding.ivIllusUserLevel)
                }
            }
        }
    }


    private fun setBeganTest() {
        binding.ivBannerVeganTest.setOnClickListener {
            mainNavigationHandler.navigateHomeToVeganTest()
        }
    }

    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun getLocation() {
        locationManager = ContextCompat.getSystemService(
            requireContext(),
            LocationManager::class.java
        ) as LocationManager

        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let {
            val latitude = location.latitude
            val longitude = location.longitude
            val accuracy = location.accuracy
            val time = location.time
            logMessage("getLocation\nlatitude = $latitude,\nlongitude = $longitude\nlocation = $location,\naccuracy = $accuracy,\ntime = $time")
        }
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 위치 정보가 변경될 때 호출되는 콜백
                logMessage("onLocationChanged")
                logMessage("${location.latitude} ${location.latitude}")
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 위치 제공자 상태 변경 시 호출되는 콜백
                logMessage("onStatusChanged")
            }

            override fun onProviderEnabled(provider: String) {
                // 위치 제공자가 사용 가능할 때 호출되는 콜백
                logMessage("onProviderEnabled")
            }

            override fun onProviderDisabled(provider: String) {
                // 위치 제공자가 사용 불가능할 때 호출되는 콜백
                logMessage("onProviderDisabled")
            }
        }
    }

    private fun getFineLocation() {
//        try {
//            logMessage("getFineLocation granted")
//            locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                5000L, // 5초
//                10f, // 10미터,
//                locationListener
//            )
//        } catch (e: SecurityException) {
//            logMessage("Location permission not granted")
//            showPermissionDeniedDialog()
//        }
    }

    private fun setTipsTab() {
        replaceFragment(HomeTipsMagazineFragment())
        tipsNowTab = "MAGAZINE"

        binding.tlTips.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceFragment(HomeTipsMagazineFragment())
                        tipsNowTab = "MAGAZINE"
                    }

                    1 -> {
                        replaceFragment(HomeTipsRecipeFragment())
                        tipsNowTab = "RECIPE"
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        setTipsMoreButton()
    }

    private fun setTipsMoreButton() {
        binding.btnTipsMore.setOnClickListener {
            when (tipsNowTab) {
                "MAGAZINE" -> {
                    mainNavigationHandler.navigateToTips()
                }

                "RECIPE" -> {
                    mainViewModel.setTipsMoveToRecipe(true)
                    mainNavigationHandler.navigateToTips()
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fl_tips_content, fragment)
            .commit()
    }


    /**
     * 권한 요청
     */
    private fun checkPermissions(permissionList: List<String>) {

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
                getLocation()
            }

            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                logMessage("checkAndRequestPermissions 대략적인 위치 권한 승인")
                getLocation()
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
                } else {
                    logMessage(
                        "shouldShowRequestPermissionRationale = false"
                    )
                    locationPermissionLauncher.launch(permissions)
                }

            }
        }
    }

    //     권한 재요청
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

    // 권한 허용 안함
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

    private fun showFineLocationDialog() {
        val dialog = DefaultDialog.Builder()
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

    companion object {
        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }


    //알림 권한 설정
    private fun requestNotificationPermission() {
        // Android 13 이상일 경우에만 알림 권한 요청
        Timber.d("requestNotificationPermission 알림 권한 요청 실행 ")
        Timber.d("Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU: ${Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                // 이미 권한이 부여된 경우
                // 권한이 이미 부여된 경우 처리할 로직
                Timber.d("이미 권한이 부여된 경우")
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                // 권한 요청의 필요성을 설명하는 다이얼로그를 표시
                Timber.d("권한 요청의 필요성을 설명하는 다이얼로그를 표시")
                showPermissionRationale()
            } else {
                // 권한 요청
                Timber.d("권한 요청")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    private fun showPermissionRationale() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("알림 권한 요청")
//            .setMessage("'비긴, 비건'에서 알림을 보내도록 허용하시겠습니까?")
//            .setPositiveButton("허용") { _, _ ->
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                    REQUEST_NOTIFICATION_PERMISSION
//                )
//            }
//            .setNegativeButton("허용 안함"){ _, _ ->
//                MypagePushAlertDialog(permit = false, mypage = false).show(childFragmentManager, "RefusePushDialog")
//            }
//            .show()
        DefaultDialog.Builder()
            .setTitle("알림 권한 요청")
            .setBody("'비긴, 비건'에서 알림을 보내도록 허용하시겠습니까?")
            .setPositiveButton("허용") {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }.setNegativeButton("허용 안함") {
                MypagePushAlertDialog(permit = false, mypage = false).show(
                    childFragmentManager,
                    "RefusePushDialog"
                )
            }.show(childFragmentManager, "RefusePushDialog")
    }
}