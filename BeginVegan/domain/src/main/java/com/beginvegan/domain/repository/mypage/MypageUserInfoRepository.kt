package com.beginvegan.domain.repository.mypage

import com.beginvegan.domain.model.mypage.MypageUserInfo

interface MypageUserInfoRepository {
    suspend fun getMypageUserInfo(): Result<MypageUserInfo>
}