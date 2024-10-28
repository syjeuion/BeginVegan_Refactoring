package com.beginvegan.presentation.view.tips.viewModel.state

import com.beginvegan.domain.model.tips.TipsRecipeListItem

data class RecipeListState(
    val response: MutableList<TipsRecipeListItem> = mutableListOf(),
    val isLoading: Boolean = false
)
