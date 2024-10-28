package com.beginvegan.data.model.inform

import com.squareup.moshi.Json

data class InformModifyRestaurantRequest(
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "content")
    val content: String
)