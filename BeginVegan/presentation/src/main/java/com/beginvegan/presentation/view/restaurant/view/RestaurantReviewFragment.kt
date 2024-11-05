package com.beginvegan.presentation.view.restaurant.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.beginvegan.data.model.map.RestaurantType
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentWriteReviewBinding
import com.beginvegan.presentation.util.UiState
import com.beginvegan.presentation.view.restaurant.viewModel.RestaurantReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantReviewFragment :
    BaseFragment<FragmentWriteReviewBinding>(FragmentWriteReviewBinding::inflate) {

    private val args: RestaurantReviewFragmentArgs by navArgs()
    private val viewModel: RestaurantReviewViewModel by viewModels()

    override fun init() {
        // 리뷰 작성 상태 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.writeReviewState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // 로딩 상태 처리 (예: 로딩 스피너 표시)
                        logMessage("리뷰 업데이트 중")
                    }

                    is UiState.Success -> {
                        // 성공 상태 처리 (예: 성공 메시지 및 화면 이동)
                        val result = Bundle()
                        result.putBoolean("isSuccess", true)
                        setFragmentResult("writeReviewSuccess", result)
                        logMessage("리뷰 작성 성공!")
                        findNavController().popBackStack()
                    }

                    is UiState.Failure -> {
                        // 실패 상태 처리 (예: 에러 메시지 표시)
                        val result = Bundle()
                        result.putBoolean("isSuccess", false)
                        setFragmentResult("writeReviewSuccess", result)
                        logMessage("리뷰 작성 성공!")
                        logMessage(state.msg)
                    }

                    is UiState.Empty -> {
                    }
                }
            }
        }


        args.let {
            binding.tvRestaurantName.text = it.name
            binding.tvRestaurantType.text = RestaurantType.getKoreanNameFromEng(it.type)
            binding.tvRestaurantAddress.text =
                "${it.province} ${it.city} ${it.roadName} ${it.detailAddress ?: ""}"
        }

        binding.tietInputReview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isReviewTextValid = s?.length ?: 0 >= 1
                val isRatingValid = binding.rbRating.rating > 0.0f
                binding.btnSubmitReview.isEnabled = isReviewTextValid && isRatingValid
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.rbRating.setOnRatingBarChangeListener { _, rating, _ ->
            val isReviewTextValid = binding.tietInputReview.text?.length ?: 0 >= 1
            val isRatingValid = rating > 0.0f

            binding.btnSubmitReview.isEnabled = isReviewTextValid && isRatingValid
        }

        binding.btnAddImg.setOnClickListener {
            showToast("이미지 추가")
        }
        binding.btnSubmitReview.setOnClickListener {
            showToast("리뷰 작성")
            val rate = binding.rbRating.rating.toDouble()
            val content = binding.tietInputReview.text.toString()
//            viewModel.writeReview(args.id, content, rate, emptyList())
        }
    }

}