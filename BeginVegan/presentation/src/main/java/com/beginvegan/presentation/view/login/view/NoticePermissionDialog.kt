package com.beginvegan.presentation.view.login.view

import android.Manifest
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogNoticePermissionBinding
import com.beginvegan.presentation.databinding.IncludeDialogPermissionBinding

class NoticePermissionDialog: BaseDialogFragment<DialogNoticePermissionBinding>(DialogNoticePermissionBinding::inflate) {
    private val REQUEST_NOTIFICATION_PERMISSION = 1

    override fun init() {
        setUI()
        setOnClickOk()
    }

    private fun setUI(){
        with(binding){
            setIncludedUI(includedLocation, R.drawable.ic_pin_stroke, getString(R.string.permission_location), getString(R.string.permission_location_description))
            setIncludedUI(includedCamera, R.drawable.ic_camera, getString(R.string.permission_camera), getString(R.string.permission_camera_description))
            setIncludedUI(includedStorage, R.drawable.ic_folder, getString(R.string.permission_storage), getString(R.string.permission_storage_description))
            setIncludedUI(includedNotification, R.drawable.ic_notification_stroke, getString(R.string.permission_notification), getString(R.string.permission_notification_description))
            setIncludedUI(includedCall, R.drawable.ic_general_call, getString(R.string.permission_call), getString(R.string.permission_call_description))
        }
    }
    private fun setIncludedUI(view:IncludeDialogPermissionBinding,img:Int, title:String, body:String){
        view.ivPermissionImage.background = ContextCompat.getDrawable(requireContext(), img)
        view.tvTitle.text = title
        view.tvBody.text = body
    }
    private fun setOnClickOk() {
        binding.btnPermissionDialogOk.setOnClickListener {
            this.dismiss()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
            // OS 12 이하는 Notification Permission 추가하기
        }
    }
}