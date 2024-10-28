package com.beginvegan.data.mapper.tips

import com.beginvegan.data.model.tips.TipsRecipeListItemDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.tips.TipsRecipeListItem

class TipsRecipeMapper: Mapper<TipsRecipeListItemDto, TipsRecipeListItem> {
    override fun mapFromEntity(type: TipsRecipeListItemDto): TipsRecipeListItem = TipsRecipeListItem(
        id = type.id,
        name = type.name,
        veganType = type.veganType,
        isBookmarked = type.isBookmarked
    )

    fun mapToRecipeList(dtoList: List<TipsRecipeListItemDto>): List<TipsRecipeListItem>{
        return dtoList.map { mapFromEntity(it) }
    }
}