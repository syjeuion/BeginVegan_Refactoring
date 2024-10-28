package com.beginvegan.data.retrofit.fcm

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.fcm.FcmMessageRequest
import com.beginvegan.data.model.fcm.HasFcmTokenResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface FcmService {

    @GET("/api/v1/users/fcm")
    suspend fun getHasFcmToken(
        @Header("Authorization") token: String
    ): ApiResponse<HasFcmTokenResponse>

    @PATCH("/api/v1/users/fcm/token")
    suspend fun patchFcmToken(
        @Header("Authorization") token: String,
        @Body fcmToken: String
    ): ApiResponse<BaseResponse>

    @POST("/api/v1/fcm/send")
    suspend fun postFcmMessage(
//        @Header("Authorization") token: String,
        @Body postFcmMessageRequest: FcmMessageRequest
    ): ApiResponse<BaseResponse>
}