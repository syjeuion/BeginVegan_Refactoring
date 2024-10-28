package com.beginvegan.data.mapper.mypage

import com.beginvegan.data.model.mypage.MyMagazineItemDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem

class MypageMyMagazineMapper:Mapper<List<MyMagazineItemDto>, List<MypageMyMagazineItem>> {
    override fun mapFromEntity(type: List<MyMagazineItemDto>): List<MypageMyMagazineItem> {
        return type.map { MypageMyMagazineItem(
            magazineId = it.magazineId,
            title = it.title,
            writeTime = it.writeTime,
            thumbnail = it.thumbnail,
            editor = it.editor
        ) }
    }
}