package com.beginvegan.presentation.util

import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogLoadingBinding

const val LOADING = "LOADING"
class LoadingDialog : BaseDialogFragment<DialogLoadingBinding>(DialogLoadingBinding::inflate) {
    companion object{
        fun newInstance() = LoadingDialog()
    }
    override fun init() {
        setCancelAble(false)
    }
}