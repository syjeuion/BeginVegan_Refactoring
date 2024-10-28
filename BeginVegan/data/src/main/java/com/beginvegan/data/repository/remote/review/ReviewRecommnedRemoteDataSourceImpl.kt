package com.beginvegan.data.repository.remote.review

import com.beginvegan.data.model.review.ReviewRecommendResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.review.ReviewService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class ReviewRecommnedRemoteDataSourceImpl @Inject constructor(
    private val reviewService: ReviewService,
    private val authTokenDataSource: AuthTokenDataSource,
):ReviewRecommendRemoteDataSource {
    override suspend fun updateReviewRecommend(reviewId: Int): ApiResponse<ReviewRecommendResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return reviewService.updateReviewRecommend(authHeader, reviewId).suspendOnSuccess {
            Timber.d("updateReviewRecommend successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("updateReviewRecommend error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }
}