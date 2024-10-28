package com.beginvegan.data.model.review

import com.squareup.moshi.Json

data class ReviewRecommendResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: List<ReviewRecommend>
)
data class ReviewRecommend(
    @Json(name = "recommendationCount")
    val recommendationCount:Int,
    @Json(name = "recommendation")
    val recommendation:Boolean
)