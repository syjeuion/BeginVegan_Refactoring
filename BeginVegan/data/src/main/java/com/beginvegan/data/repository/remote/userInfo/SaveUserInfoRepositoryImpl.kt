package com.beginvegan.data.repository.remote.userInfo

import com.beginvegan.data.mapper.core.BaseMapper
import com.beginvegan.data.model.userInfo.AddUserInfoReq
import com.beginvegan.data.model.userInfo.AddUserInfoRequest
import com.beginvegan.domain.model.core.BasicResult
import com.beginvegan.domain.repository.userInfo.SaveUserInfoRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import timber.log.Timber
import javax.inject.Inject

class SaveUserInfoRepositoryImpl @Inject constructor(
    private val saveUserInfoDataSource: SaveUserInfoDataSource,
    private val baseMapper: BaseMapper
) : SaveUserInfoRepository {
    override suspend fun updateUserInfo(
        nickName: String,
        veganType: String,
        isDefaultImage: Boolean,
        imageUri: String?
    ): Result<BasicResult> {
        return try {
            val addUserInfoReq = AddUserInfoReq(nickName,veganType)
            // viewModel에서 이미지 여부 따라 받아올 것
            val addUserInfoRequest = AddUserInfoRequest(addUserInfoReq,isDefaultImage)
            val response = saveUserInfoDataSource.updateUserInfo(addUserInfoRequest,imageUri)
            when (response) {
                is ApiResponse.Success -> {
                    val authToken = baseMapper.mapFromEntity(response.data)
                    Result.success(authToken)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("SignIn error: ${response.errorBody}")
                    Result.failure(Exception("SignIn failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("SignIn exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "SignIn exception")
            Result.failure(e)
        }
    }

}