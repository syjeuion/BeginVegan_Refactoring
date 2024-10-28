package com.beginvegan.presentation.view.tips.viewModel.state

import com.beginvegan.domain.model.tips.TipsMagazineItem

data class MagazineListState(
    val response: MutableList<TipsMagazineItem>? = null,
    val isLoading: Boolean = false
)
