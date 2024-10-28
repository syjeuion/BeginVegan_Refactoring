package com.beginvegan.data.repository.remote.mypage

import com.beginvegan.data.model.mypage.MypagePushResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.mypage.MypagePushService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class MypagePushRemoteDataSourceImpl @Inject constructor(
    private val mypagePushService: MypagePushService,
    private val authTokenDataSource: AuthTokenDataSource,
):MypagePushRemoteDataSource {
    override suspend fun getPushState(): ApiResponse<MypagePushResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return mypagePushService.getPushState(authHeader).suspendOnSuccess {
            Timber.d("getPushState successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("getPushState error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }

    override suspend fun patchPush(): ApiResponse<MypagePushResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return mypagePushService.patchPush(authHeader).suspendOnSuccess {
            Timber.d("patchPush successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("patchPush error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }
}