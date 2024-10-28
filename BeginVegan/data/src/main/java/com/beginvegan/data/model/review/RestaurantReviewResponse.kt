package com.beginvegan.data.model.review

import com.squareup.moshi.Json

data class RestaurantReviewResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<Review>
)

data class Review(
    @Json(name = "reviewId")
    val reviewId: Long,
    @Json(name = "user")
    val user: Reviewer,
    @Json(name = "reviewType")
    val reviewType: String,
    @Json(name = "imageUrl")
    val imageUrl: List<String>,
    @Json(name = "rate")
    val rate: Float,
    @Json(name = "data")
    val date: String,
    @Json(name = "content")
    val content: String,
    @Json(name = "visible")
    val visible: Boolean,
    @Json(name = "recommendationCount")
    val recommendationCount: Int,
    @Json(name = "recommendation")
    val recommendation: Boolean
)

data class Reviewer(
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "userCode")
    val userCode: String,
    @Json(name = "level")
    val level: String
)