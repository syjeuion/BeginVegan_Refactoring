package com.beginvegan.data.repository.remote.review

import com.beginvegan.data.model.review.ReviewRecommendResponse
import com.skydoves.sandwich.ApiResponse

interface ReviewRecommendRemoteDataSource {
    suspend fun updateReviewRecommend(reviewId:Int): ApiResponse<ReviewRecommendResponse>
}