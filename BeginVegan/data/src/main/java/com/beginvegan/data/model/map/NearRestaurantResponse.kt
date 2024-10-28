package com.beginvegan.data.model.map

import com.squareup.moshi.Json

data class NearRestaurantResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<NearRestaurantDto>
)

data class NearRestaurantDto(
    @Json(name = "restaurantId")
    val restaurantId: Int,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "bookmark")
    val bookmark: Boolean
)