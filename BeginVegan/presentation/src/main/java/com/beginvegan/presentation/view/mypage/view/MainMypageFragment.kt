package com.beginvegan.presentation.view.mypage.view

import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.beginvegan.domain.model.mypage.MypageUserInfo
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.databinding.FragmentMainMypageBinding
import com.beginvegan.presentation.util.DrawerController
import com.beginvegan.presentation.util.MypageUserLevelExplainDialog
import com.beginvegan.presentation.util.UserLevelLists
import com.beginvegan.presentation.view.mypage.viewModel.MypageUserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


////import com.bumptech.glide.Glide
//import com.example.beginvegan.R
//import com.example.beginvegan.config.ApplicationClass
//import com.example.beginvegan.config.BaseFragment
//import com.example.beginvegan.databinding.FragmentMainMypageBinding
//import com.example.beginvegan.util.LogoutDialog
//import com.example.beginvegan.util.ProfileEditNameDialog
//import com.example.beginvegan.util.ProfileEditVeganTypeDialog
//import com.example.presentation.base.BaseFragment
//
//class MainMypageFragment : BaseFragment<FragmentMainMypageBinding>(
//    FragmentMainMypageBinding::bind, R.layout.fragment_main_mypage
//), BottomSheetLogoutFragment.MyFragmentInteractionListener,
//    ProfileEditNameDialog.EditNameDialogListener,
//    ProfileEditVeganTypeDialog.EditVeganTypeDialogListener {
//    override fun init() {
////
////        //ViewPager
////        val vpMyRecords = binding.vpMyRecords
////        vpMyRecords.adapter = ProfileMyRecordsVPAdapter(this)
////        //tab
////        val tabLayout = binding.tlRecords
////        TabLayoutMediator(tabLayout, vpMyRecords) { tab, pos ->
////            when (pos) {
////                0 -> tab.text = "나의 리뷰"
////                1 -> tab.text = "나의 스크랩"
////            }
////        }.attach()
////        //시작 시 유저 이름 반영
////        binding.tvUsername.text = ApplicationClass.xAuth.name
////
////        //닉네임 수정 dialog
////        binding.ibEditUsername.setOnClickListener {
////            openEditUserNameDialog()
////        }
////        //Vegan Type 수정 dialog
////        binding.ibEditVeganType.setOnClickListener {
////            openEditVeganTypeDialog()
////        }
////        //테스트 바로가기
////        binding.bGoVeganTest.setOnClickListener {
////            val intent = Intent(this.context, VeganTestActivity::class.java)
////            startActivity(intent)
////        }
////        //로그아웃 more button 클릭
////        binding.btnProfileMore.setOnClickListener {
////            openBottomSheetLogout()
////        }
////        binding.tvUsername.text = ApplicationClass.xAuth.name
//////        binding.tvUserVeganType.text = VeganTypes.valueOf("${ApplicationClass.xAuth.veganType}").veganType
////        if (ApplicationClass.xAuth.imageUrl != null) {
////            Glide.with(requireContext()).load(ApplicationClass.xAuth.imageUrl)
////                .into(binding.civProfileImage)
////        }
//    }
////
//    //닉네임 수정
////    private fun openEditUserNameDialog() { //dialog 띄우기
////        val editNameDialog =
////            ProfileEditNameDialog(requireContext(), binding.tvUsername.text.toString())
////        editNameDialog.setListener(this)
////        editNameDialog.show()
////    }
//
//    override fun editNameOnSaveClicked(name: String) { //수정한 name UI 반영
//        ApplicationClass.xAuth.name = name //이름 변경
////        binding.tvUsername.text = name
//    }
////
//    //비건 유형 수정
////    private fun openEditVeganTypeDialog() { //dialog 띄우기
////        val editVeganTypeDialog =
////            ProfileEditVeganTypeDialog(requireContext(), binding.tvUserVeganType.text.toString())
////        editVeganTypeDialog.setListener(this)
////        editVeganTypeDialog.show()
////    }
////
//    override fun editVeganTypeOnSaveClicked(type: String) {
////        binding.tvUserVeganType.text = type
//    }
//
////    //로그아웃
////    private fun openBottomSheetLogout() { //bottom sheet 열기
////        val bottomSheet = BottomSheetLogoutFragment()
////        bottomSheet.listener = this
////        bottomSheet.show(requireActivity().supportFragmentManager, null)
////    }
//
//        fun openLogoutDialog() { //dialog 띄우기
//            val logoutDialog = LogoutDialog(requireContext())
//            logoutDialog.show()
//        }
//
//    override fun onButtonClicked() { //dialog 로그아웃 버튼 클릭
//        openLogoutDialog()
//    }
//}

@AndroidEntryPoint
class MainMypageFragment : BaseFragment<FragmentMainMypageBinding>(R.layout.fragment_main_mypage) {

    @Inject
    lateinit var drawerController: DrawerController
    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val mypageUserInfoViewModel:MypageUserInfoViewModel by viewModels()
    private lateinit var veganTypeKr:Array<String>
    private lateinit var veganTypeEng:Array<String>

    override fun init() {
        binding.vm = mypageUserInfoViewModel
        binding.lifecycleOwner = this

        setOpenDrawer()
        openDialogUserLevelExplain()
        moveFuns()

        getUserInfo()
    }

    private fun getUserInfo(){
        mypageUserInfoViewModel.userInfo.observe(this){
            setUserInfo(it)
        }
    }
    private fun setUserInfo(userInfo:MypageUserInfo){
        val userLevelKr = resources.getStringArray(R.array.user_levels_kr)
        val userLevelEng = resources.getStringArray(R.array.user_levels_eng)
        veganTypeKr = resources.getStringArray(R.array.vegan_type)
        veganTypeEng = resources.getStringArray(R.array.vegan_types_eng)
        val userLevelLists = UserLevelLists(requireContext())
        val levelIllusts = userLevelLists.userLevelIllus
        val levelIcons = userLevelLists.userLevelIcons
        val maxPoints = userLevelLists.userLevelMaxPoint

        val userLevelIndex = userLevelEng.indexOf(userInfo.userLevel)
        Glide.with(this)
            .load(levelIllusts[userLevelIndex])
            .into(binding.ivIllusUserLevel)
        binding.tvUserLevel.text = "${userLevelKr[userLevelIndex]} 레벨"

        Glide.with(this)
            .load(userInfo.imageUrl)
            .transform(CircleCrop())
            .into(binding.ivUserProfileImg)

        Glide.with(this)
            .load(levelIcons[userLevelIndex])
            .into(binding.ivUserProfileUserLevel)

        binding.tvUserName.text = userInfo.nickname

        setProgressBar(maxPoints[userLevelIndex],userInfo.point)
        Timber.d("maxPoints[index]:${maxPoints[userLevelIndex]},userInfo.point:${userInfo.point}")

        val veganTypelIndex = veganTypeEng.indexOf(userInfo.veganType)
        setVeganTypeDropdown(veganTypeKr[veganTypelIndex])
    }
    private fun setProgressBar(maxInt: Int, nowGauge: Int) {
        binding.pbUserLevelExp.max = maxInt
        binding.pbUserLevelExp.progress = nowGauge
    }
    private fun setVeganTypeDropdown(userVeganType: String) {
        val dropdownView = binding.actvMypageEditVegantype
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown_mypage_set_vegan_type,
            resources.getStringArray(R.array.vegan_type)
        )
        adapter.setDropDownViewResource(R.layout.item_dropdown_mypage_set_vegan_type)
        dropdownView.setAdapter(adapter)
        dropdownView.hint = userVeganType

        binding.ivDropdownIcon.setOnClickListener {
            dropdownView.showDropDown()
        }
        dropdownView.setOnClickListener {
            dropdownView.showDropDown()
        }
        dropdownView.setOnItemClickListener { parent, view, position, id ->
//            Timber.d("position:$position, selected Item: ${veganTypeKr[position]}")
            mypageUserInfoViewModel.patchUserVeganType(veganTypeEng[position])
        }
    }

    private fun moveFuns(){
        binding.llEditProfile.setOnClickListener {
            mainNavigationHandler.navigateMypageToEditProfile()
        }
        binding.llMyReview.setOnClickListener {
            mainNavigationHandler.navigateMypageToMyReview()
        }
        binding.llMyRestaurant.setOnClickListener {
            mainNavigationHandler.navigateMypageToMyRestaurant()
        }
        binding.llMyMagazine.setOnClickListener {
            mainNavigationHandler.navigateMypageToMyMagazine()
        }
        binding.llMyRecipe.setOnClickListener {
            mainNavigationHandler.navigateMypageToMyRecipe()
        }
        binding.llSetting.setOnClickListener {
            mainNavigationHandler.navigateMypageToMySetting()
        }
    }
    private fun setOpenDrawer() {
        binding.includedToolbar.ibNotification.setOnClickListener {
            drawerController.openDrawer()
        }
    }

    private fun openDialogUserLevelExplain() {
        binding.llUserLevelExplain.setOnClickListener {
            val dialog = MypageUserLevelExplainDialog(requireContext())
            dialog.show()
        }
    }

}