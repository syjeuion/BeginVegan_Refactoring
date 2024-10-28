package com.beginvegan.data.model.mypage

import com.squareup.moshi.Json

data class MyReviewResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<MyReviewDto>
)

data class MyReviewDto(
    @Json(name = "reviewId") val reviewId:Int,
    @Json(name = "restaurantName") val restaurantName:String,
    @Json(name = "date") val date:String,
    @Json(name = "rate") val rate:Float,
    @Json(name = "images") val images:List<String>,
    @Json(name = "content") val content:String,
    @Json(name = "countRecommendation") val countRecommendation:Int,
    @Json(name = "recommendation") val recommendation:Boolean
)