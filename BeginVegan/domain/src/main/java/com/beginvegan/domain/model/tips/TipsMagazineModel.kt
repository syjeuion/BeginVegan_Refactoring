package com.beginvegan.domain.model.tips


data class TipsMagazineList(
    val tipsMagazineList: List<TipsMagazineItem>
)

data class TipsMagazineItem(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val editor: String,
    val createdDate: String,
    val isBookmarked: Boolean
)

data class TipsMagazineDetail(
    val id: Int,
    val title: String,
    val editor: String,
    val source: String?,
    val thumbnail: String,
    var isBookmarked: Boolean,
    val createdDate: String,
    val magazineContents: List<MagazineContent>
)

data class MagazineContent(
    val content: String,
    val sequence: Int,
    val isBold:Boolean
)