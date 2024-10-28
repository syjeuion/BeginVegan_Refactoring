package com.beginvegan.data.model.tips

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MagazineResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<TipsMagazineItemDto>
)

//data class TipsMagazineListDto(
//    @Json(name = "tipsMagazineList")
//    val tipsMagazineList: List<TipsMagazineItemDto>
//)

data class TipsMagazineItemDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "editor") val editor: String,
    @Json(name = "createdDate") val createdDate: String,
    @Json(name = "isBookmarked") val isBookmarked: Boolean
)


