package com.beginvegan.data.repository.local.fcm

import kotlinx.coroutines.flow.Flow

interface FcmTokenDataSource {
    suspend fun saveFcmToken(fcmToken:String)
    val fcmToken: Flow<String?>
}