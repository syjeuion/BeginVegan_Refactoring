package com.beginvegan.presentation.view.restaurant.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beginvegan.domain.useCase.review.RestaurantReviewUseCase
import com.beginvegan.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantReviewViewModel @Inject constructor(
    private val restaurantReviewUseCase: RestaurantReviewUseCase
) : ViewModel() {

    // API 처리 상태를 관리하기 위한 StateFlow
    private val _writeReviewState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val writeReviewState: StateFlow<UiState<Boolean>> = _writeReviewState

    // 리뷰 작성 메소드
    fun writeReview(restaurantReviewId: Long, content: String, rate: Double, files: List<String?>) {
        viewModelScope.launch {
            _writeReviewState.value = UiState.Loading // 로딩 상태로 전환

            try {
                // 리뷰 작성 UseCase 호출
                restaurantReviewUseCase.writeReview(restaurantReviewId, content, rate, files)
                // 성공 시 상태 업데이트
                _writeReviewState.value = UiState.Success(true)
            } catch (e: Exception) {
                // 실패 시 상태 업데이트
                _writeReviewState.value = UiState.Failure(e.message ?: "Unknown Error")
            }
        }
    }
}