package com.beginvegan.data.model.userInfo

import com.squareup.moshi.Json

data class HomeUserInfoResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: HomeUserInfoDto
)

data class HomeUserInfoDto(
    @Json(name = "nickname") val nickname:String,
    @Json(name = "userLevel") val userLevel:String
)
