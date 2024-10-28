package com.beginvegan.data.model.userInfo

import com.squareup.moshi.Json

data class AddUserInfoRequest(
    @Json(name = "addUserInfoReq")
    val addUserInfoReq: AddUserInfoReq,
    @Json(name= "isDefaultImage")
    val isDefaultImage: Boolean,
)
