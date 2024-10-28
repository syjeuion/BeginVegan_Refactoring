package com.beginvegan.data.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VeganMapRestaurantResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<VeganMapRestaurantDto>
)
@JsonClass(generateAdapter = true)
data class VeganMapRestaurantDto(
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "restaurantName")
    val restaurantName: String,
    @Json(name = "restaurantType")
    val restaurantType: RestaurantType?,
    @Json(name = "distance")
    val distance: Double,
    @Json(name = "rate")
    val rate: Double?,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "latitude")
    val latitude: String,
    @Json(name = "longitude")
    val longitude: String
)