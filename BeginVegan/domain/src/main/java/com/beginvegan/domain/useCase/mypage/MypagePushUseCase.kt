package com.beginvegan.domain.useCase.mypage

import com.beginvegan.domain.repository.mypage.MypagePushRepository
import javax.inject.Inject

class MypagePushUseCase @Inject constructor(
    private val mypagePushRepository: MypagePushRepository
){
    suspend fun getPushState(): Result<Boolean> = mypagePushRepository.getPushState()
    suspend fun patchPush(): Result<Boolean> = mypagePushRepository.patchPush()
}