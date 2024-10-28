package com.beginvegan.data.retrofit.mypage

import com.beginvegan.data.model.mypage.MypagePushResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MypagePushService {
    @GET("/api/v1/users/alarm")
    suspend fun getPushState(
        @Header("Authorization") token: String
    ): ApiResponse<MypagePushResponse>

    @PATCH("/api/v1/users/alarm")
    suspend fun patchPush(
        @Header("Authorization") token: String
    ): ApiResponse<MypagePushResponse>
}