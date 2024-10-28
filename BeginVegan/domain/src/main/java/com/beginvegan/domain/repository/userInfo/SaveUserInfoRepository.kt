package com.beginvegan.domain.repository.userInfo

import com.beginvegan.domain.model.core.BasicResult

interface SaveUserInfoRepository {
    suspend fun updateUserInfo(
        nickName: String,
        veganType: String,
        isDefaultImage: Boolean,
        imageUri: String?
    ): Result<BasicResult>
}