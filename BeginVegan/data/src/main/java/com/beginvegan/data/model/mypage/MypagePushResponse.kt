package com.beginvegan.data.model.mypage

import com.squareup.moshi.Json

data class MypagePushResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information:PushResponse
)

data class PushResponse(
    @Json(name = "alarmSetting")
    val alarmSetting:Boolean
)