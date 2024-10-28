package com.beginvegan.presentation.view.mypage.viewModel.state

import com.beginvegan.domain.model.mypage.MyReview

data class MyReviewState(
    val response: List<MyReview>? = null,
    val isLoading: Boolean = false
)
