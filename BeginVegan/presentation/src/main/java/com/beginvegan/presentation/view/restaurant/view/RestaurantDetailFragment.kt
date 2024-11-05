package com.beginvegan.presentation.view.restaurant.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.beginvegan.data.model.map.RestaurantType
import com.beginvegan.domain.model.map.RestaurantDetail
import com.beginvegan.domain.model.review.RestaurantReview
import com.beginvegan.presentation.NavRestaurantGraphArgs
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.customView.CheckboxView
import com.beginvegan.presentation.databinding.FragmentRestaurantDetailBinding
import com.beginvegan.presentation.util.DefaultDialog
import com.beginvegan.presentation.util.NoTitleDialog
import com.beginvegan.presentation.util.UiState
import com.beginvegan.presentation.view.restaurant.adapter.MenuRVAdapter
import com.beginvegan.presentation.view.restaurant.adapter.ReviewRVAdapter
import com.beginvegan.presentation.view.restaurant.viewModel.RestaurantViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.tabs.TabLayout
import com.kakao.vectormap.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantDetailFragment :
    BaseFragment<FragmentRestaurantDetailBinding>(FragmentRestaurantDetailBinding::inflate) {
    private val args: NavRestaurantGraphArgs by navArgs()

    private lateinit var restaurantMenuAdapter: MenuRVAdapter

    private lateinit var restaurantReviewAdapter: ReviewRVAdapter

    private val viewModel: RestaurantViewModel by viewModels()

    private var currentLocation: LatLng? = null

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private var isLocationUpdateRequested: Boolean = false

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult?.let { result ->
                for (location in result.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    logMessage("Location Update: Latitude = $latitude, Longitude = $longitude")
                    // 현재 위치 저장
                    currentLocation = LatLng.from(latitude, longitude)

                    break
                }
            }
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


    override fun init() {

        args?.let {
            viewModel.getRestaurantDetail(it.restaurantId, it.latitude, it.longitude)
        }

        requestLocationUpdates()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.restaurantDetail.filterNot { it == RestaurantDetail() }.collect {
                initRestaurantDetail(it)
            }
        }

        collectMenuList()

        collectReviewList()

        viewModel.isMoreMenus.observe(this) {
            if (viewModel.isMoreMenus.value!!) {
            } else {
            }
        }
        viewModel.isMoreReviews.observe(this) {
            if (viewModel.isMoreReviews.value!!) {

            } else {
            }

        }

        binding.toggleMenuMore.setOnCheckedChangeListener {
            moreMenuList(binding.toggleMenuMore.check)

        }
        binding.toggleReviewMore.setOnCheckedChangeListener {
            moreReviewList(binding.toggleReviewMore.check)
        }

        setFragmentResultListener("writeReviewSuccess") { _, bundle ->
            val isSuccess = bundle.getBoolean("isSuccess")
            if (isSuccess) {
                initRestaurantDetail(viewModel.restaurantDetail.value)
            } else {
                showToast("리뷰 작성 실패")
            }
        }

        setPageNavigationButton()

        collectDeleteUiState()

        setFabBtn()

        setFilterTab()

        setPhotoFilter()
    }

    private fun setPhotoFilter() {
        binding.cbPhotoFilter.setOnCheckedChangeListener(object :
            CheckboxView.OnCheckedChangeListener {
            override fun onCheckedChanged(view: CheckboxView, isChecked: Boolean) {
                val selectFilter = binding.tlReviewFilter.selectedTabPosition
                when (selectFilter) {
                    0 -> {
                        viewModel.getRestaurantReviewList(
                            viewModel.restaurantDetail.value.restaurantId,
                            0,
                            isChecked,
                            "date"
                        )
                    }

                    1 -> {
                        viewModel.getRestaurantReviewList(
                            viewModel.restaurantDetail.value.restaurantId,
                            0,
                            isChecked,
                            "recommendation"
                        )
                    }
                }

            }

        })
    }

    private fun setFilterTab() {
        binding.tlReviewFilter.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        viewModel.getRestaurantReviewList(
                            viewModel.restaurantDetail.value.restaurantId,
                            0,
                            binding.cbPhotoFilter.isChecked,
                            "date"
                        )
                    }

                    1 -> {
                        viewModel.getRestaurantReviewList(
                            viewModel.restaurantDetail.value.restaurantId,
                            0,
                            binding.cbPhotoFilter.isChecked,
                            "recommendation"
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun collectDeleteUiState() {
        lifecycleScope.launch {
            viewModel.deleteState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // 로딩 상태 처리 (예: 로딩 스피너 표시)
                        logMessage("리뷰 삭제 중...")
                    }

                    is UiState.Success -> {
                        // 성공 상태 처리 (예: 성공 메시지 및 화면 이동)
                        showToast("리뷰 삭제 성공")
                        logMessage("리뷰 삭제 성공")
                        findNavController().popBackStack()
                    }

                    is UiState.Failure -> {
                        // 실패 상태 처리 (예: 에러 메시지 표시)
                        showToast("리뷰 삭제 실패")
                        logMessage("리뷰 삭제 실패")
                        logMessage(state.msg)
                    }

                    is UiState.Empty -> {
                    }
                }
            }
        }
    }

    private fun collectMenuList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.menuList.collect {
                if (it.isNotEmpty()) {
                    if (it.size > 3) {
                        binding.toggleMenuMore.visibility = View.VISIBLE
                    }
                    setMenuList()
                }
            }
        }
    }

    private fun collectReviewList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviewList.collect {
                if (it.isNotEmpty()) {
                    if (it.size > 3) {
                        binding.toggleReviewMore.visibility = View.VISIBLE
                    }
                    binding.tvEmptyView.visibility = View.GONE
                } else {
                    binding.tvEmptyView.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun initRestaurantDetail(data: RestaurantDetail) {
        binding.tvTitleRestaurantName.text = data.name
        if (args.imgUrl.isNullOrBlank()) {
            Glide.with(requireContext()).load(R.drawable.image_error)
                .into(binding.ivRestaurantImg)
        } else {
            Glide.with(requireContext()).load(args.imgUrl).error(R.drawable.image_error)
                .into(binding.ivRestaurantImg)
        }

        binding.tvRestaurantName.text = data.name
        binding.tvRestaurantType.text = RestaurantType.getKoreanNameFromEng(data.restaurantType)
        binding.tvRestaurantAddress.text =
            "${data.address.province} ${data.address.city} ${data.address.roadName} ${data.address.detailAddress ?: ""}"
        binding.tvRestaurantScore.text = if (data.rate == null) {
            "0.0"
        } else {
            String.format("%.1f", data.rate)
        }
        binding.tvRestaurantReviewCount.text = "(${data.reviewCount})"

        binding.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }

        // 리뷰 작성하기
        binding.btnWriteReview.setOnClickListener {
            val action =
                RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToRestaurantReviewFragment(
                    id = data.restaurantId,
                    name = data.name,
                    type = data.restaurantType,
                    province = data.address.province,
                    city = data.address.city,
                    roadName = data.address.roadName,
                    detailAddress = data.address.detailAddress ?: "",
                )
            findNavController().navigate(action)
        }

        setMenuList()

        setDefaultOptionReviewList(data.restaurantId)

        viewModel.userInfo.observe(this) {
            setReviewList(it.id)
        }

    }

    private fun setDefaultOptionReviewList(restaurantId: Long) {
        viewModel.getRestaurantReviewList(restaurantId, 0, false, FILTER_DATE)
    }

    private fun setReviewList(userId: Long) {

        restaurantReviewAdapter = ReviewRVAdapter(userId)

        binding.rvReview.adapter = restaurantReviewAdapter
        binding.rvReview.itemAnimator = null
        if (viewModel.reviewList.value.size > 3) {
            restaurantReviewAdapter.submitList(viewModel.reviewList.value.take(3))
        } else {
            restaurantReviewAdapter.submitList(viewModel.reviewList.value)
        }

        restaurantReviewAdapter.setOnEditListener(object : ReviewRVAdapter.OnEditClickListener {
            override fun onEdit(data: RestaurantReview) {
                showToast("리뷰 수정")
            }

        })
        restaurantReviewAdapter.setOnDeleteListener(object : ReviewRVAdapter.OnDeleteClickListener {
            override fun onDelete(data: RestaurantReview) {
                DefaultDialog.Builder()
                    .setTitle("지급된 리워드가 회수될 수 있어요.\n리뷰를 삭제하시겠어요?")
                    .setPositiveButton("확인") {
                        viewModel.deleteRestaurantReview(data.reviewId)
                    }.setNegativeButton("취소") {
                    }.show(childFragmentManager, "defaultDialogTag")

            }
        })
        restaurantReviewAdapter.setOnReportListener(object : ReviewRVAdapter.OnReportClickListener {
            override fun onReport(data: RestaurantReview) {
                RestaurantReviewReportDialog().show(
                    childFragmentManager,
                    "RestaurantReviewReportDialog"
                )
            }
        })
    }

    private fun setMenuList() {
        restaurantMenuAdapter = MenuRVAdapter()

        binding.rvMenu.adapter = restaurantMenuAdapter

        binding.rvMenu.itemAnimator = null

        if (viewModel.menuList.value.size > 3) {
            restaurantMenuAdapter.submitList(viewModel.menuList.value.take(3))
        } else {
            restaurantMenuAdapter.submitList(viewModel.menuList.value)
        }


    }

    private fun moreMenuList(more: Boolean) {
        if (more) {
            restaurantMenuAdapter.submitList(viewModel.menuList.value)
        } else {
            restaurantMenuAdapter.submitList(viewModel.menuList.value.take(3))
        }

    }

    private fun moreReviewList(more: Boolean) {
        if (more) {
            restaurantReviewAdapter.submitList(viewModel.reviewList.value)
        } else {
            restaurantReviewAdapter.submitList(viewModel.reviewList.value.take(3))
        }
    }

    private fun setPageNavigationButton() {
        binding.btnNavFind.setOnClickListener {
            NoTitleDialog.Builder()
                .setBody(
                    "카카오맵으로 이동하시겠어요?"
                )
                .setPositiveButton("확인") {
                    searchLoadToKakaoMap()
                }.setNegativeButton("취소") {
                    logMessage("닫기")
                }.show(childFragmentManager, "KakaoRoadMapDialog")
        }
        binding.btnNavCall.setOnClickListener {
            val restaurantNumber = viewModel.restaurantDetail.value.contactNumber
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$restaurantNumber")
            startActivity(dialIntent)
        }
        binding.btnNavLike.setOnClickListener {
            showToast("준비중: 저장")
        }
        binding.btnNavEdit.setOnClickListener {
            RestaurantModifyInformationDialog(viewModel.restaurantDetail.value.restaurantId).show(
                childFragmentManager,
                "RestaurantModifyInformationDialog"
            )
        }
    }

    private fun searchLoadToKakaoMap() {
        val startLatitude = currentLocation?.latitude
        val startLongitude = currentLocation?.latitude

        val endLatitude = args.latitude
        val endLongitude = args.longitude

        val url =
            "kakaomap://route?sp=$startLatitude,$startLongitude$&ep=$endLatitude,$endLongitude&by=PUBLICTRANSIT"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setPackage("net.daum.android.map")

        val installCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().packageManager.queryIntentActivities(
                Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            requireContext().packageManager.queryIntentActivities(
                Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                PackageManager.GET_META_DATA
            )
        }

        if (installCheck.isEmpty()) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=net.daum.android.map")
                )
            )
        } else {
            startActivity(intent)
        }
    }

    companion object {
        // 최신순
        const val FILTER_DATE = "date"

        // 추천순
        const val FILTER_RECOMMENDATION = "recommendation"
    }

    private fun setFabBtn() {
        binding.nsvScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0) {
                binding.ibFab.visibility = View.VISIBLE
            } else {
                binding.ibFab.visibility = View.GONE
            }
        }


        binding.ibFab.setOnClickListener {
            binding.nsvScrollView.fullScroll(ScrollView.FOCUS_UP)
        }
    }
}