package com.beginvegan.domain.repository.mypage


interface MypagePushRepository {
    suspend fun getPushState(): Result<Boolean>
    suspend fun patchPush(): Result<Boolean>
}