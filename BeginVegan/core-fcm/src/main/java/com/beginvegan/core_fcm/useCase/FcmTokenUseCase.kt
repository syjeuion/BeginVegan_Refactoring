package com.beginvegan.core_fcm.useCase

import com.beginvegan.core_fcm.repository.FcmTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenUseCase @Inject constructor(
    private val fcmTokenRepository: FcmTokenRepository
) {
    fun resetToken(){
        FirebaseMessaging.getInstance().deleteToken()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 토큰이 삭제됨. 새로운 토큰이 onNewToken()에서 발급됨.
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            Timber.d("deleteToken: 토큰 삭제 성공")
                        }
                    }
                } else {
                    // 에러 처리
                    Timber.e("DeleteToken: 토큰 삭제 실패 ${task.exception}")
                }
            }
        Timber.d("FirebaseMessaging.getInstance().token: ${FirebaseMessaging.getInstance().token}")
    }

    suspend fun saveToken(token: String) {
        fcmTokenRepository.saveToken(token)
    }

    suspend fun getHasFcmToken():Result<Boolean>{
        return fcmTokenRepository.getHasFcmToken()
    }

    suspend fun postFcmMessage(
        title: String,
        body: String,
        alarmType: String?,
        itemId: Int?,
        messageType: String?,
        userLevel: String?
    ) = fcmTokenRepository.postFcmMessage(title, body, alarmType, itemId, messageType, userLevel)
}