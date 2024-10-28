package com.beginvegan.data.repository.remote.fcm

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.fcm.FcmMessageRequest
import com.beginvegan.data.model.fcm.HasFcmTokenResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.fcm.FcmService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class FcmRemoteDataSourceImpl @Inject constructor(
    private val fcmService: FcmService,
    private val authTokenDataSource: AuthTokenDataSource,
):FcmRemoteDataSource {
    override suspend fun getHasFcmToken(): ApiResponse<HasFcmTokenResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return fcmService.getHasFcmToken(authHeader).suspendOnSuccess {
            Timber.d("getHasFcmToken successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("getHasFcmToken error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }

    override suspend fun patchFcmToken(token: String): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return fcmService.patchFcmToken(authHeader, token).suspendOnSuccess {
            Timber.d("patchFcmToken successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("patchFcmToken error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }

    override suspend fun postFcmMessage(requestBody:FcmMessageRequest): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return fcmService.postFcmMessage(requestBody).suspendOnSuccess {
            Timber.d("postFcmMessage successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("postFcmMessage error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }
}