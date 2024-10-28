package com.beginvegan.domain.model.review

data class RestaurantReview(
    val reviewId: Long,
    val user: Reviewer,
    val reviewType: String,
    val imageUrl: List<String>,
    val rate: Float,
    val date: String,
    val content: String,
    val visible: Boolean,
    val recommendationCount: Int,
    val recommendation: Boolean
)

data class Reviewer(
    val userId: Long,
    val imageUrl: String,
    val nickname: String,
    val userCode: String,
    val level: String
)
