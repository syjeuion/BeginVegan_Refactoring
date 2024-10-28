package com.beginvegan.data.repository.remote.review

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.review.RestaurantReviewResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.review.ReviewService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class RestaurantReviewRemoteDataSourceImpl @Inject constructor(
    private val reviewService: ReviewService,
    private val authTokenDataSource: AuthTokenDataSource
) : RestaurantReviewRemoteDataSource {
    override suspend fun getRestaurantReview(
        restaurantId: Long,
        page: Int,
        isPhoto: Boolean,
        filter: String
    ): ApiResponse<RestaurantReviewResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return reviewService.getRestaurantReview(authHeader,restaurantId, page, isPhoto, filter)
            .suspendOnSuccess {
                Timber.d("getRestaurantReview successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("getRestaurantReview error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun writeReview(
        restaurantId: Long,
        content: String,
        rate: Double,
        imgUri: List<String?>
    ): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"

        val multipartList = imgUri.filterNotNull().map { uri ->
            val file = File(uri)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files", file.name, requestBody)
        }
        return reviewService.postWriteReview(authHeader, restaurantId, content, rate, multipartList)
            .suspendOnSuccess {
                Timber.d("writeReview successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("writeReview error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun reportReview(reviewId: Long, content: String): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return reviewService.postReportReview(authHeader, reviewId, content)
            .suspendOnSuccess {
                Timber.d("reportReview successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("reportReview error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun deleteReview(reviewId: Long): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return reviewService.deleteReview(authHeader, reviewId)
            .suspendOnSuccess {
                Timber.d("deleteReview successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("deleteReview error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }
}