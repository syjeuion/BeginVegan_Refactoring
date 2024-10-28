package com.beginvegan.data.model.fcm

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FcmMessageRequest(
    @Json(name = "token") val token:String,
    @Json(name = "title") val title:String,
    @Json(name = "body") val body:String,
    @Json(name = "alarmType") val alarmType:String?,
    @Json(name = "itemId") val itemId:Int?,
    @Json(name = "messageType") val messageType:String?,
    @Json(name = "userLevel") val userLevel:String?
)
