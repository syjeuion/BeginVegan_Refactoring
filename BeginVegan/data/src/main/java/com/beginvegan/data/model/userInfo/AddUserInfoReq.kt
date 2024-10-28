package com.beginvegan.data.model.userInfo

import com.squareup.moshi.Json

data class AddUserInfoReq(
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "veganType")
    val veganType: String,
)
