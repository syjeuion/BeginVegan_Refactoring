package com.beginvegan.data.repository.remote.mypage

import com.beginvegan.data.model.mypage.MypageUserInfoResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.mypage.MypageUserInfoService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class MypageUserInfoRemoteDataSourceImpl @Inject constructor(
    private val mypageUserInfoService: MypageUserInfoService,
    private val authTokenDataSource: AuthTokenDataSource
):MypageUserInfoRemoteDataSource {
    override suspend fun getMypageUserInfo(): ApiResponse<MypageUserInfoResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return mypageUserInfoService.getMypageUserInfo(authHeader).suspendOnSuccess {
            Timber.d("getMypageUserInfo successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("getMypageUserInfo error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }
}