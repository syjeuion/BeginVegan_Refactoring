package com.beginvegan.presentation.view.mypage.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentMypageSettingBinding
import com.beginvegan.presentation.util.setToolbar
import com.beginvegan.presentation.view.mypage.viewModel.MypagePushViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MypageSettingFragment : BaseFragment<FragmentMypageSettingBinding>(FragmentMypageSettingBinding::inflate) {
    private val mypagePushViewModel: MypagePushViewModel by viewModels()

    override fun init() {
        setBackUp()
        setPushToggle()
        setLogOut()
        setDeleteAccount()
    }
    //Push 알림 설정
    private fun setPushToggle(){
        mypagePushViewModel.getPushState()

        mypagePushViewModel.userPushState.observe(this){
            binding.scPushSwitch.setOnCheckedChangeListener(null)
            binding.scPushSwitch.isChecked = it

            //Patch
            binding.scPushSwitch.setOnCheckedChangeListener { _, isChecked ->
                mypagePushViewModel.patchPush()
                MypagePushAlertDialog(isChecked, true).show(childFragmentManager, "RefusePushDialog")
                if(isChecked){
                    requestNotificationPermission()
                }else{

                }
            }
        }
    }

    //알림 권한 설정
    private fun requestNotificationPermission() {
        // Android 13 이상일 경우에만 알림 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.POST_NOTIFICATIONS)) {
                // 권한 요청의 필요성을 설명하는 다이얼로그를 표시
                Timber.d("권한 요청의 필요성을 설명하는 다이얼로그를 표시")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
//                showPermissionRationale()
            } else {
                // 권한 요청
                Timber.d("권한 요청")
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = android.net.Uri.parse("package:" +requireContext() .packageName)
                startActivity(intent)
            }
        }
    }

    //로그아웃
    private fun setLogOut(){
        binding.tvLogout.onThrottleClick{
            openDialogLogout()
        }
    }
    private fun openDialogLogout(){
        MypageLogoutDialog().show(childFragmentManager, "LogoutDialog")
        MypageLogoutDialog().setOnConfirm(object : MypageLogoutDialog.OnBtnClickListener {
            override fun onConfirm() {
                //확인 클릭 시
            }
        })
    }

    //계정삭제
    private fun setDeleteAccount(){
        binding.tvDeleteAccount.onThrottleClick {
            openDialogDeleteAccount()
        }
    }
    private fun openDialogDeleteAccount(){
        MypageDeleteAccountDialog {
            onConfirm
        }.show(childFragmentManager, "DeleteAccountDialog")
    }
    private val onConfirm = {
        logMessage("계정삭제")
    }


    //backStack
    private fun setBackUp(){
        binding.ibBackUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}