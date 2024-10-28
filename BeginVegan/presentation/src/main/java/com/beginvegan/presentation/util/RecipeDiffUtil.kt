package com.beginvegan.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.beginvegan.domain.model.tips.TipsRecipeListItem

class RecipeDiffUtil(
    private val oldItemList: List<TipsRecipeListItem>,
    private val newItemList: List<TipsRecipeListItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItemList.size

    override fun getNewListSize(): Int = newItemList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemList[oldItemPosition] == newItemList[newItemPosition]
    }
}