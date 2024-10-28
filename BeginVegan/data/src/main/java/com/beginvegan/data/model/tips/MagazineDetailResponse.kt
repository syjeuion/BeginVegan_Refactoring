package com.beginvegan.data.model.tips

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MagazineDetailResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: TipsMagazineDetailDto
)

data class TipsMagazineDetailDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "editor") val editor: String,
    @Json(name = "source") val source: String?,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "isBookmarked") val isBookmarked: Boolean,
    @Json(name = "createdDate") val createdDate: String,
    @Json(name = "magazineContents") val magazineContents: List<MagazineContentDto>
)

data class MagazineContentDto(
    @Json(name = "content") val content: String,
    @Json(name = "sequence") val sequence: Int,
    @Json(name = "isBold") val isBold: Boolean
)