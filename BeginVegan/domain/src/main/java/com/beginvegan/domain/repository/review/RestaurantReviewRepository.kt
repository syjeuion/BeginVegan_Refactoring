package com.beginvegan.domain.repository.review

import com.beginvegan.domain.model.core.BasicResult
import com.beginvegan.domain.model.review.RestaurantReview
import kotlinx.coroutines.flow.Flow

interface RestaurantReviewRepository {
    suspend fun getReviewByRestaurantId(
        restaurantId: Long,
        page: Int,
        isPhoto: Boolean,
        filter: String
    ): Flow<List<RestaurantReview>>

    suspend fun writeReview(
        restaurantId: Long,
        content: String,
        rate: Double,
        files: List<String?>
    ): Result<BasicResult>

    suspend fun reportReview(reviewId: Long, content: String): Result<BasicResult>
    suspend fun deleteReview(reviewId: Long): Result<BasicResult>
}