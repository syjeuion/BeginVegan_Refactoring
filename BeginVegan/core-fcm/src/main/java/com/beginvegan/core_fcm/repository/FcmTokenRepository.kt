package com.beginvegan.core_fcm.repository

interface FcmTokenRepository {
    suspend fun getHasFcmToken():Result<Boolean>
    suspend fun saveToken(token: String)
    suspend fun postFcmMessage(title:String, body:String, alarmType:String?, itemId:Int?, messageType:String?, userLevel:String?)
}