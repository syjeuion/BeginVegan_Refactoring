package com.beginvegan.presentation.view.mypage.viewModel.state

import com.beginvegan.domain.model.tips.TipsRecipeListItem

data class MyRecipeState(
    val response: MutableList<TipsRecipeListItem>? = null,
    val isLoading: Boolean = false
)
