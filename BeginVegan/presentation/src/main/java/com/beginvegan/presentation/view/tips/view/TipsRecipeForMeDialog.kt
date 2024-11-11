package com.beginvegan.presentation.view.tips.view

import android.view.ViewGroup
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogWithTitleBinding

class TipsRecipeForMeDialog:BaseDialogFragment<DialogWithTitleBinding>(DialogWithTitleBinding::inflate) {

    override fun init() {
        isCancelable = false

        with(binding){
            tvTitle.text = getString(R.string.dialog_tips_recipe_for_me_title)
            tvContent.text = getString(R.string.dialog_tips_recipe_for_me_description)
            btnConfirm.text = getString(R.string.btn_confirm)

            tvSubContent.visibility = ViewGroup.GONE
            btnCancel.visibility = ViewGroup.GONE

            btnConfirm.setOnClickListener {
                dismiss()
            }
        }
    }
}