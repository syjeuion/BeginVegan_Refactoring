package com.beginvegan.domain.useCase.userInfo

import com.beginvegan.domain.model.userInfo.HomeUserInfo
import com.beginvegan.domain.repository.userInfo.HomeUserInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeUserInfoUseCase @Inject constructor(
    private val homeUserInfoRepository: HomeUserInfoRepository
){
    suspend operator fun invoke(): Flow<HomeUserInfo> {
        return homeUserInfoRepository.getHomeUserInfo()
    }
}