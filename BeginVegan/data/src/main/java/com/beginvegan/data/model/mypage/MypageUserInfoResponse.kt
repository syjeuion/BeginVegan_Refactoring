package com.beginvegan.data.model.mypage

import com.squareup.moshi.Json

data class MypageUserInfoResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: MypageUserInfoDto
)

data class MypageUserInfoDto(
    @Json(name = "id") val id: Long,
    @Json(name = "imageUrl") val imageUrl:String,
    @Json(name = "nickname") val nickname:String,
    @Json(name = "userLevel") val userLevel:String,
    @Json(name = "veganType") val veganType:String,
    @Json(name = "point") val point:Int
)
