package com.beginvegan.data.repository.remote.review

import com.beginvegan.domain.repository.review.ReviewRecommendRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import timber.log.Timber
import javax.inject.Inject

class ReviewRecommendRepositoryImpl @Inject constructor(
    private val reviewRecommendRemoteDataSource: ReviewRecommendRemoteDataSource
):ReviewRecommendRepository {
    override suspend fun updateReviewRecommend(reviewId: Int): Result<Boolean> {
        return try {
            val response = reviewRecommendRemoteDataSource.updateReviewRecommend(reviewId)
            when (response) {
                is ApiResponse.Success -> {
                    Result.success(true)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("updateReviewRecommend error: ${response.errorBody}")
                    Result.failure(Exception("updateReviewRecommend failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("updateReviewRecommend exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "updateReviewRecommend exception")
            Result.failure(e)
        }
    }
}