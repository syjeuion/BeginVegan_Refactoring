package com.beginvegan.presentation.view.mypage.viewModel.state

import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem

data class MyRestaurantState(
    val response: List<MypageMyRestaurantItem>? = null,
    val isLoading: Boolean = false
)
