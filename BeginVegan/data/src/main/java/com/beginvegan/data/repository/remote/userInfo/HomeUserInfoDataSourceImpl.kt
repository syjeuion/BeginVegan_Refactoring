package com.beginvegan.data.repository.remote.userInfo

import com.beginvegan.data.model.userInfo.HomeUserInfoResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.auth.UserInfoService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class HomeUserInfoDataSourceImpl @Inject constructor(
    private val getHomeUserInfo: UserInfoService,
    private val authTokenDataSource: AuthTokenDataSource
): HomeUserInfoDataSource{
    override suspend fun getHomeUserInfo(): ApiResponse<HomeUserInfoResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"

        return getHomeUserInfo.getHomeUserInfo(
            authHeader
        )  .suspendOnSuccess {
            Timber.d("getHomeUserInfo successful${this.data}")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("getHomeUserInfo error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }

}