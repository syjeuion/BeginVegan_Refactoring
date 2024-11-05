package com.beginvegan.presentation.view.restaurant.view

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogRestaurantEditInfoBinding
import com.beginvegan.presentation.util.UiState
import com.beginvegan.presentation.view.restaurant.viewModel.RestaurantModifyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantModifyInformationDialog(
    private val restaurantId: Long
) : BaseDialogFragment<DialogRestaurantEditInfoBinding>(DialogRestaurantEditInfoBinding::inflate) {

    private val viewModel: RestaurantModifyViewModel by viewModels()

    override fun init() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.modifyState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // 로딩 상태 처리 (예: 로딩 스피너 표시)
                        logMessage("식당 정보 수정중..")
                    }

                    is UiState.Success -> {
                        // 성공 상태 처리 (예: 성공 메시지 및 화면 이동)
                        showToast("식당 정보 수정 성공")
                        logMessage("식당 정보 수정 성공!")
                        dismiss()
                    }

                    is UiState.Failure -> {
                        // 실패 상태 처리 (예: 에러 메시지 표시)
                        showToast("식당 정보 수정 실패")
                        logMessage("식당 정보 수정 실패!")
                        logMessage(state.msg)
                    }

                    is UiState.Empty -> {
                        logMessage("default")
                    }
                }
            }
        }
        binding.tietModify.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSubmitModify.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.btnClose.setOnClickListener {
            this.dismiss()
        }
        binding.btnSubmitModify.setOnClickListener {
            viewModel.modifyRestaurant(restaurantId, binding.tietModify.text.toString())
        }
    }
}