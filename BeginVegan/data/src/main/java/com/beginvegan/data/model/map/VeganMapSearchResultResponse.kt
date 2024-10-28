package com.beginvegan.data.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VeganMapSearchResultResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<VeganMapSearchResultDto>
)
@JsonClass(generateAdapter = true)
data class VeganMapSearchResultDto(
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "restaurantType")
    val restaurantType: RestaurantType?,
    @Json(name = "distance")
    val distance: Double,
    @Json(name = "rate")
    val rate: Double?,
    @Json(name = "latitude")
    val latitude: String,
    @Json(name = "longitude")
    val longitude: String
)