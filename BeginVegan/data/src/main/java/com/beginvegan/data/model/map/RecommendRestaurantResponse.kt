package com.beginvegan.data.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecommendRestaurantResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<RecommendRestaurantInformation>
)
@JsonClass(generateAdapter = true)
data class RecommendRestaurantInformation(
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "bookmark")
    val bookmark: Boolean,
    @Json(name = "latitude")
    val latitude: String,
    @Json(name = "longitude")
    val longitude: String
)