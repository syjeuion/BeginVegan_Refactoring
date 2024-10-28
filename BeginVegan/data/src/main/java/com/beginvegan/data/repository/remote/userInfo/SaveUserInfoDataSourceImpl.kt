package com.beginvegan.data.repository.remote.userInfo

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.userInfo.AddUserInfoRequest
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.auth.UserInfoService
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class SaveUserInfoDataSourceImpl @Inject constructor(
    private val userInfoService: UserInfoService,
    private val authTokenDataSource: AuthTokenDataSource
) : SaveUserInfoDataSource {

    override suspend fun updateUserInfo(
        addUserInfoRequest: AddUserInfoRequest,
        imageUri: String?
    ): ApiResponse<BaseResponse> {
        val multiPartBody: MultipartBody.Part =
            if (imageUri != null) {
                val file = File(imageUri)
                val requestBody = file.asRequestBody("file".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", file.name, requestBody)
            } else {
                // 빈 RequestBody 생성
                val requestBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", "", requestBody)
            }

        // `accessToken`을 가져와서 헤더로 추가
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"

        return userInfoService.updateUserInfo(
            authHeader,
            addUserInfoRequest.addUserInfoReq,
            addUserInfoRequest.isDefaultImage,
            multiPartBody
        )
    }
}
