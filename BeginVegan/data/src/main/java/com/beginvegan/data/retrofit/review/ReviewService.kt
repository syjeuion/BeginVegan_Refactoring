package com.beginvegan.data.retrofit.review

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.review.RestaurantReviewResponse
import com.beginvegan.data.model.review.ReviewRecommendResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewService {
    // 리뷰 추천
    @POST("/api/v1/reviews/{reviewId}/recommendation")
    suspend fun updateReviewRecommend(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Int
    ): ApiResponse<ReviewRecommendResponse>

    // 식당 리뷰 조회
    @GET("/api/v1/restaurants/{restaurantId}/review")
    suspend fun getRestaurantReview(
        @Header("Authorization") token: String,
        @Path("restaurantId") restaurantId: Long,
        @Query("page") page: Int,
        @Query("isPhoto") isPhoto: Boolean,
        @Query("filter") filter: String
    ): ApiResponse<RestaurantReviewResponse>

    // 리뷰 작성
    @Multipart
    @POST("/api/v1/restaurants/{restaurantId}/review")
    suspend fun postWriteReview(
        @Header("Authorization") token: String,
        @Part("restaurantId") restaurantId: Long,
        @Part("content") content: String,
        @Part("rate") rate: Double,
        @Part("files") files: List<MultipartBody.Part?>
    ): ApiResponse<BaseResponse>

    // 리뷰 신고
    @POST("/api/v1/reviews/{reviewId}/report")
    suspend fun postReportReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Long,
        @Body content: String
    ): ApiResponse<BaseResponse>

    // 리뷰 삭제
    @DELETE("/api/v1/reviews/{reviewId}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: Long,
    ): ApiResponse<BaseResponse>
}