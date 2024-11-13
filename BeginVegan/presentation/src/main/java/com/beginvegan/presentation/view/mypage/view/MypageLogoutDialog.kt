package com.beginvegan.presentation.view.mypage.view

import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogWithoutTitleBinding

class MypageLogoutDialog:BaseDialogFragment<DialogWithoutTitleBinding>(DialogWithoutTitleBinding::inflate){
    private var listener: OnBtnClickListener? = null

    override fun init() {
//        isCancelable =false
        with(binding){
            tvContent.text = getString(R.string.dialog_mypage_setting_logout)
            btnCancel.text = getString(R.string.btn_cancel)
            btnConfirm.text = getString(R.string.btn_confirm)

            btnCancel.setOnClickListener {
                dismiss()
            }
            btnConfirm.setOnClickListener {
                listener?.onConfirm()
            }
        }
    }

    interface OnBtnClickListener{
        fun onConfirm()
    }
    fun setOnConfirm(listener: OnBtnClickListener){
        this.listener = listener
    }
}