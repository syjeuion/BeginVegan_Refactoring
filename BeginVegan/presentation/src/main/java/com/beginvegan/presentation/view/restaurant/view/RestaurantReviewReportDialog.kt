package com.beginvegan.presentation.view.restaurant.view

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogReviewReportBinding
import com.beginvegan.presentation.view.restaurant.viewModel.RestaurantReviewReportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantReviewReportDialog :
    BaseDialogFragment<DialogReviewReportBinding>(DialogReviewReportBinding::inflate) {

    private val viewModel: RestaurantReviewReportViewModel by viewModels()
    override fun init() {

        binding.rgReportReason.setOnCheckedChangeListener { _, checkedId ->
            updateSubmitButtonState()
        }

        binding.tietReviewReport.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSubmitButtonState()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.btnClose.setOnClickListener {
            this.dismiss()
        }
    }

    private fun updateSubmitButtonState() {
        val selectedReportId = binding.rgReportReason.checkedRadioButtonId
        val isOtherSelected = selectedReportId == binding.rbEtc.id
        val isReportEtc = binding.tietReviewReport.text?.isNotBlank() == true

        binding.btnSubmitReport.isEnabled = !isOtherSelected || (isOtherSelected && isReportEtc)

    }
}