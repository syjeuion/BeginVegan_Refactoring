package com.beginvegan.presentation.util

import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogLoadingBinding

class LoadingDialog: BaseDialogFragment<DialogLoadingBinding>(R.layout.dialog_loading) {
    override fun init() {
        setDim(0.7f)
        setCancelAble(false)
    }

}