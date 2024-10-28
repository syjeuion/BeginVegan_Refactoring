package com.beginvegan.presentation.view.mypage.view

import android.view.View
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogPermissionRefuseNotificationBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MypagePushAlertDialog(private val permit:Boolean, private val mypage:Boolean):BaseDialogFragment<DialogPermissionRefuseNotificationBinding>(R.layout.dialog_permission_refuse_notification) {
    override fun init() {
        if(permit) setBinding("동의", "거부")
        else setBinding("거부", "동의")

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }
    private fun setBinding(type: String, noType:String){
        binding.tvTitle.text = getString(R.string.permission_refuse_notification_title, type)
        binding.tvContentContent.text = getString(R.string.permission_refuse_notification_content, type)

        if(mypage) binding.tvSubContent.visibility = View.GONE
        else binding.tvSubContent.text = getString(R.string.permission_refuse_notification_description, noType)

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        binding.tvDateContent.text = currentDate.format(formatter)
    }
}