package com.beginvegan.data.mapper.tips

import com.beginvegan.data.model.tips.TipsMagazineItemDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.tips.TipsMagazineItem

class TipsMagazineMapper:Mapper<List<TipsMagazineItemDto>, List<TipsMagazineItem>> {
    override fun mapFromEntity(type: List<TipsMagazineItemDto>): List<TipsMagazineItem> {
        return type.map { mapItem(it) }
    }

    private fun mapItem(type: TipsMagazineItemDto): TipsMagazineItem{
        return TipsMagazineItem(
            id = type.id,
            title = type.title,
            thumbnail = type.thumbnail,
            editor = type.editor,
            createdDate = type.createdDate,
            isBookmarked = type.isBookmarked
        )
    }
}