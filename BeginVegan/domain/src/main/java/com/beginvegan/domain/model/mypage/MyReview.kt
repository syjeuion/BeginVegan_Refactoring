package com.beginvegan.domain.model.mypage

data class MyReview(
    val reviewId:Int,
    val restaurantName:String,
    val date:String,
    val rate:Float,
    val images:List<String>,
    val content:String,
    val countRecommendation:Int,
    val recommendation:Boolean
)
