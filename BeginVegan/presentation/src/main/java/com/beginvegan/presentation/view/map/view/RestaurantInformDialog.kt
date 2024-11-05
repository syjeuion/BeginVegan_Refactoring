package com.beginvegan.presentation.view.map.view

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogRestaurantInformBinding
import com.beginvegan.presentation.util.UiState
import com.beginvegan.presentation.view.map.viewModel.RestaurantInformViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantInformDialog :
    BaseDialogFragment<DialogRestaurantInformBinding>(DialogRestaurantInformBinding::inflate) {

    private val viewModel: RestaurantInformViewModel by viewModels()

    override fun init() {

        // 리뷰 작성 상태 관찰
        lifecycleScope.launch {
            viewModel.informState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // 로딩 상태 처리 (예: 로딩 스피너 표시)
                        logMessage("식당 제보 중..")
                    }

                    is UiState.Success -> {
                        // 성공 상태 처리 (예: 성공 메시지 및 화면 이동)
                        showToast("식당 제보 성공")
                        logMessage("식당 제보 성공!")
                        dismiss()
                    }

                    is UiState.Failure -> {
                        // 실패 상태 처리 (예: 에러 메시지 표시)
                        showToast("식당 제보 실패")
                        logMessage("식당 제보 실패!")
                        logMessage(state.msg)
                    }

                    is UiState.Empty -> {
                    }

                    else -> {

                    }
                }
            }
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        binding.tietReportName.addTextChangedListener(textWatcher)
        binding.tietReportLocation.addTextChangedListener(textWatcher)
        binding.tietReportExplanation.addTextChangedListener(textWatcher)

        binding.btnSubmitModify.setOnClickListener {
            val name = binding.tietReportName.text.toString()
            val location = binding.tietReportLocation.text.toString()
            val explanation = binding.tietReportExplanation.text.toString()
            viewModel.informNewRestaurant(name, location, explanation)
        }

        binding.btnBackUp.setOnClickListener {
            dismiss()
        }
    }

    private fun validateInput() {
        val isNameValid = binding.tietReportName.text?.isNotEmpty() == true
        val isLocationValid = binding.tietReportLocation.text?.isNotEmpty() == true
        val isExplanation = binding.tietReportExplanation.text?.isNotEmpty() == true

        binding.btnSubmitModify.isEnabled = isNameValid && isLocationValid && isExplanation
    }
}