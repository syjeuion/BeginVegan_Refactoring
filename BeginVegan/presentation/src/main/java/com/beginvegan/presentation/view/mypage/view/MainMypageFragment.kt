package com.beginvegan.presentation.view.mypage.view

import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
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
import com.beginvegan.presentation.util.setToolbar
import com.beginvegan.presentation.view.mypage.viewModel.MypageUserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainMypageFragment :
    BaseFragment<FragmentMainMypageBinding>(FragmentMainMypageBinding::inflate) {

    @Inject
    lateinit var drawerController: DrawerController

    @Inject
    lateinit var mainNavigationHandler: MainNavigationHandler
    private val mypageUserInfoViewModel: MypageUserInfoViewModel by viewModels()
    private lateinit var veganTypeKr: Array<String>
    private lateinit var veganTypeEng: Array<String>

    override fun init() {
        setOpenDrawer()
        openDialogUserLevelExplain()
        moveFuns()
        setUI()
        getUserInfo()
    }
    private fun setUI(){
        with(binding.includedToolbar){
            ibNotification.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_notification_stroke)
            tvTitle.text = getString(R.string.mypage_title)
        }
        setTipsToolbar()
    }
    private fun setTipsToolbar(){
        setToolbar(
            view = binding.includedToolbar,
            title = getString(R.string.mypage_title),
            backColor = ContextCompat.getColor(requireContext(), R.color.color_white),
            contentColor = ContextCompat.getColor(requireContext(), R.color.color_primary),
            isBackIcon = false,
            isNotiIcon = true
        )
    }

    private fun getUserInfo() {
        mypageUserInfoViewModel.userInfo.observe(this) {
            setUserInfo(it)
        }
    }

    private fun setUserInfo(userInfo: MypageUserInfo) {
        val userLevelKr = requireNotNull(resources.getStringArray(R.array.user_levels_kr))
        val userLevelEng = requireNotNull(resources.getStringArray(R.array.user_levels_eng))
        veganTypeKr = requireNotNull(resources.getStringArray(R.array.vegan_type))
        veganTypeEng = requireNotNull(resources.getStringArray(R.array.vegan_types_eng))
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

        setProgressBar(maxPoints[userLevelIndex], userInfo.point)
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

    private fun moveFuns() {
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