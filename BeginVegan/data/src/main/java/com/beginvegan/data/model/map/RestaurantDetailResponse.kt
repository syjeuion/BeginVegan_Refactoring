package com.beginvegan.data.model.map

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RestaurantDetailResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: RestaurantInformation
)
@JsonClass(generateAdapter = true)
data class RestaurantInformation(
    @Json(name = "restaurant")
    val restaurant: RestaurantDto,
    @Json(name = "menus")
    val menus: List<MenuDto>
)
@JsonClass(generateAdapter = true)
data class RestaurantDto(
    @Json(name = "restaurantId")
    val restaurantId: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "restaurantType")
    val restaurantType: String,
    @Json(name = "address")
    val address: AddressDto,
    @Json(name = "distance")
    val distance: Double,
    @Json(name = "rate")
    val rate: Double?,
    @Json(name = "reviewCount")
    val reviewCount: Int,
    @Json(name = "contactNumber")
    val contactNumber: String,
    @Json(name = "bookmark")
    val bookmark: Boolean
)

data class AddressDto(
    @Json(name = "province")
    val province: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "roadName")
    val roadName: String,
    @Json(name = "detailAddress")
    val detailAddress: String?
)

data class MenuDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)
