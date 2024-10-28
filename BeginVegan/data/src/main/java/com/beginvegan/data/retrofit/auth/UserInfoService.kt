package com.beginvegan.data.retrofit.auth

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.userInfo.AddUserInfoReq
import com.beginvegan.data.model.userInfo.HomeUserInfoResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserInfoService {
    @Multipart
    @PUT("/auth/sign-up/detail")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("addUserInfoReq") addUserInfoReq: AddUserInfoReq,
        @Part("isDefaultImage") isDefaultImage: Boolean,
        @Part file: MultipartBody.Part?
    ): ApiResponse<BaseResponse>


    @GET("/api/v1/users/home")
    suspend fun getHomeUserInfo(
        @Header("Authorization") token: String
    ): ApiResponse<HomeUserInfoResponse>
}