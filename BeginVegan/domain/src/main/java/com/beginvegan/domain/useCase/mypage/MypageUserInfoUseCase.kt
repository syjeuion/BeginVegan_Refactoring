package com.beginvegan.domain.useCase.mypage

import com.beginvegan.domain.model.mypage.MypageUserInfo
import com.beginvegan.domain.repository.mypage.MypageUserInfoRepository
import javax.inject.Inject

class MypageUserInfoUseCase @Inject constructor(
    private val mypageUserInfoRepository: MypageUserInfoRepository
){
    suspend operator fun invoke():Result<MypageUserInfo> {
        return mypageUserInfoRepository.getMypageUserInfo()
    }
}