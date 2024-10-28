package com.beginvegan.data.model.review

import com.squareup.moshi.Json

data class WriteReviewRequest (
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "content")
    val content: String,
    @Json(name = "rate")
    val rate: Double,
)