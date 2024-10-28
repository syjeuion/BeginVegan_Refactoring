package com.beginvegan.data.repository.remote.review

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.review.RestaurantReviewResponse
import com.skydoves.sandwich.ApiResponse

interface RestaurantReviewRemoteDataSource {
    suspend fun getRestaurantReview(
        restaurantId: Long,
        page: Int,
        isPhoto: Boolean,
        filter: String
    ): ApiResponse<RestaurantReviewResponse>

    suspend fun writeReview(
        restaurantId: Long,
        content: String,
        rate: Double,
        imgUri: List<String?>
    ): ApiResponse<BaseResponse>

    suspend fun reportReview(reviewId: Long, content: String): ApiResponse<BaseResponse>

    suspend fun deleteReview(reviewId: Long): ApiResponse<BaseResponse>
}