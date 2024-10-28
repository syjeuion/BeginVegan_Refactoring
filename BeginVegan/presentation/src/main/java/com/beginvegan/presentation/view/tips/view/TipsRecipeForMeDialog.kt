package com.beginvegan.presentation.view.tips.view

import android.view.ViewGroup
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogWithTitleBinding

class TipsRecipeForMeDialog:BaseDialogFragment<DialogWithTitleBinding>(R.layout.dialog_with_title) {

    override fun init() {
        isCancelable = false

        binding.title = getString(R.string.dialog_tips_recipe_for_me_title)
        binding.content = getString(R.string.dialog_tips_recipe_for_me_description)
        binding.btnConfirm.text = getString(R.string.btn_confirm)

        binding.tvSubContent.visibility = ViewGroup.GONE
        binding.btnCancel.visibility = ViewGroup.GONE

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }
}