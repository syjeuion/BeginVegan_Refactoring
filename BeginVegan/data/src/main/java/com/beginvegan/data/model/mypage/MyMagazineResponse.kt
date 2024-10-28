package com.beginvegan.data.model.mypage

import com.squareup.moshi.Json

data class MyMagazineResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<MyMagazineItemDto>
)

data class MyMagazineItemDto(
    @Json(name = "magazineId") val magazineId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "editor") val editor: String,
    @Json(name = "writeTime") val writeTime: String
)
