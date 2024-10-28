package com.beginvegan.data.model.core

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse(
    @Json(name = "check") val check: Boolean,
    @Json(name = "information") val information: Information
) {

    @JsonClass(generateAdapter = true)
    data class Information(
        @Json(name = "message") val message: String
    )
}