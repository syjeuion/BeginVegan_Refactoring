package com.beginvegan.data.mapper.mypage

import com.beginvegan.data.model.mypage.MyRecipeItemDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.tips.TipsRecipeListItem

class MypageMyRecipeMapper:Mapper<List<MyRecipeItemDto>, List<TipsRecipeListItem>> {
    override fun mapFromEntity(type: List<MyRecipeItemDto>): List<TipsRecipeListItem> {
        return type.map { TipsRecipeListItem(
            id = it.foodId,
            name = it.name,
            veganType = it.veganType,
            isBookmarked = true
        ) }
    }
}