package com.beginvegan.data.repository.remote.userInfo

import com.beginvegan.data.model.userInfo.HomeUserInfoResponse
import com.skydoves.sandwich.ApiResponse

interface HomeUserInfoDataSource {
    suspend fun getHomeUserInfo(): ApiResponse<HomeUserInfoResponse>
}