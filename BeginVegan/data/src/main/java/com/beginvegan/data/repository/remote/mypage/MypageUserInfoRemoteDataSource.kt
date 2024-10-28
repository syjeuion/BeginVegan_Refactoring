package com.beginvegan.data.repository.remote.mypage

import com.beginvegan.data.model.mypage.MypageUserInfoResponse
import com.skydoves.sandwich.ApiResponse

interface MypageUserInfoRemoteDataSource {
    suspend fun getMypageUserInfo(): ApiResponse<MypageUserInfoResponse>
}