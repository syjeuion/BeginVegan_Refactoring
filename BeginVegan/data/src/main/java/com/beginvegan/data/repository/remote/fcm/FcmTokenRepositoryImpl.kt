package com.beginvegan.data.repository.remote.fcm

import com.beginvegan.core_fcm.repository.FcmTokenRepository
import com.beginvegan.data.model.fcm.FcmMessageRequest
import com.beginvegan.data.repository.local.fcm.FcmTokenDataSource
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class FcmTokenRepositoryImpl @Inject constructor(
    private val fcmRemoteDataSource: FcmRemoteDataSource,
    private val fcmTokenDataSource: FcmTokenDataSource
): FcmTokenRepository {
    override suspend fun getHasFcmToken(): Result<Boolean> {
        return try {
            val response = fcmRemoteDataSource.getHasFcmToken()
            when (response) {
                is ApiResponse.Success -> {
                    Result.success(response.data.information.storedFcmToken)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("getHasFcmToken error: ${response.errorBody}")
                    Result.failure(Exception("getHasFcmToken failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("getHasFcmToken exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "getHasFcmToken exception")
            Result.failure(e)
        }
    }

    override suspend fun saveToken(token: String) {
        Timber.d("FCM: saveToken: $token")
        fcmRemoteDataSource.patchFcmToken(token)
        fcmTokenDataSource.saveFcmToken(token)
    }

    override suspend fun postFcmMessage(
        title: String,
        body: String,
        alarmType: String?,
        itemId: Int?,
        messageType: String?,
        userLevel: String?
    ) {
        var userFcmToken = fcmTokenDataSource.fcmToken.first()
        Timber.d("FCM: postFcmMessage: userFcmToken: $userFcmToken")
        userFcmToken?.let{
            val requestBody = FcmMessageRequest(userFcmToken, title, body, alarmType, itemId, messageType, userLevel)
            fcmRemoteDataSource.postFcmMessage(requestBody)
        }
    }
}