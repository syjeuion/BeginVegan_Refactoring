package com.beginvegan.domain.useCase.userInfo

import com.beginvegan.domain.model.core.BasicResult
import com.beginvegan.domain.repository.userInfo.SaveUserInfoRepository
import javax.inject.Inject

class SaveUserInfoUseCase @Inject constructor(private val saveUserInfoRepository: SaveUserInfoRepository) {
    suspend operator fun invoke(
        nickName: String,
        veganType: String,
        isDefaultImage: Boolean,
        imageUri: String?
    ): Result<BasicResult> =
        saveUserInfoRepository.updateUserInfo(nickName, veganType, isDefaultImage, imageUri)
}