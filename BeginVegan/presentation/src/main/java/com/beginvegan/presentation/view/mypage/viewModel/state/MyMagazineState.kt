package com.beginvegan.presentation.view.mypage.viewModel.state

import com.beginvegan.domain.model.mypage.MypageMyMagazineItem

data class MyMagazineState(
    val response: List<MypageMyMagazineItem>? = null,
    val isLoading: Boolean = false
)
