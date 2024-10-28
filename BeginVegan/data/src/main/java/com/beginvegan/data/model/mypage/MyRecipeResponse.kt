package com.beginvegan.data.model.mypage

import com.squareup.moshi.Json

data class MyRecipeResponse (
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<MyRecipeItemDto>
)
data class MyRecipeItemDto(
    @Json(name = "foodId") val foodId:Int,
    @Json(name = "name") val name:String,
    @Json(name = "veganType") val veganType:String
)
