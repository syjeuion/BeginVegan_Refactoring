package com.beginvegan.data.model.tips

import com.squareup.moshi.Json

data class TipsRecipeDetailRequest(
    @Json(name = "id")
    val id: Int,
)

data class TipsRecipeDetailResponse(
    val check: Boolean,
    val information: TipsRecipeDetailDto
)