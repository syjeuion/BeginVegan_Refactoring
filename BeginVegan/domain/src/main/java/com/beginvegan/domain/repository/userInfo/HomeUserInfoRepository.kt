package com.beginvegan.domain.repository.userInfo

import com.beginvegan.domain.model.userInfo.HomeUserInfo
import kotlinx.coroutines.flow.Flow

interface HomeUserInfoRepository {
    suspend fun getHomeUserInfo(): Flow<HomeUserInfo>
}