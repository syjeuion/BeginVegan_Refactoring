package com.beginvegan.data.repository.remote.userInfo

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.userInfo.AddUserInfoRequest
import com.skydoves.sandwich.ApiResponse

interface SaveUserInfoDataSource {
    suspend fun updateUserInfo(
        addUserInfoRequest: AddUserInfoRequest,
        imageUri: String?
    ): ApiResponse<BaseResponse>
}