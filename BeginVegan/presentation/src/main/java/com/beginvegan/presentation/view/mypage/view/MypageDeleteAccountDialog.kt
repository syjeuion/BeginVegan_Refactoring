package com.beginvegan.presentation.view.mypage.view

import androidx.core.view.isVisible
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogWithTitleBinding

class MypageDeleteAccountDialog(
    private val onConfirm: () -> Unit
) : BaseDialogFragment<DialogWithTitleBinding>(DialogWithTitleBinding::inflate) {

    override fun init() {
        with(binding) {
            tvTitle.text = getString(R.string.dialog_mypage_setting_delete_account_title)
            tvContent.text = getString(R.string.dialog_mypage_setting_delete_account_description)
            tvSubContent.isVisible = false
            btnCancel.text = getString(R.string.btn_cancel)
            btnConfirm.text = getString(R.string.btn_delete_account)

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnConfirm.setOnClickListener {
                onConfirm()
            }
        }
    }
}