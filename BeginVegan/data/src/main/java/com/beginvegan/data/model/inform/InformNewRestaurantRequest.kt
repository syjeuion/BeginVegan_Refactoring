package com.beginvegan.data.model.inform

import com.squareup.moshi.Json

data class InformNewRestaurantRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "content")
    val content: String
)
