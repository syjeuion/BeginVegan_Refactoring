package com.beginvegan.data.model.mypage

import com.squareup.moshi.Json

data class MyRestaurantResponse (
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<MyRestaurantItemDto>
)
data class MyRestaurantItemDto(
    @Json(name = "restaurantId") val restaurantId:Int,
    @Json(name = "thumbnail") val thumbnail:String,
    @Json(name = "name") val name:String,
    @Json(name = "restaurantType") val restaurantType:String,
    @Json(name = "address") val address:List<RestaurantAddressDto>
)
data class RestaurantAddressDto(
    @Json(name = "province") val province:String,
    @Json(name = "city") val city:String,
    @Json(name = "roadName") val roadName:String,
    @Json(name = "detailAddress") val detailAddress:String
)