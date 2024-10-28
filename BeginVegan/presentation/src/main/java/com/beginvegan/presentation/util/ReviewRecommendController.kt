package com.beginvegan.presentation.util

interface ReviewRecommendController {
    suspend fun updateReviewRecommend(reviewId:Int) : Boolean
}