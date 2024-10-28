package com.beginvegan.data.retrofit.auth

import com.beginvegan.data.model.auth.AuthRequest
import com.beginvegan.data.model.auth.SignInResponse
import com.beginvegan.data.model.auth.TokenRefreshResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/auth/sign-in")
    suspend fun signIn(
        @Body request: AuthRequest
    ): ApiResponse<SignInResponse>

    @POST("/auth/refresh")
    suspend fun tokenRefresh(
        @Body refreshToken: String
    ): ApiResponse<TokenRefreshResponse>
}