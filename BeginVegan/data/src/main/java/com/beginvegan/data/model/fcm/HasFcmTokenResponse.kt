package com.beginvegan.data.model.fcm

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class HasFcmTokenResponse(
    @Json(name = "check") val check: Boolean,
    @Json(name = "information") val information: Information
) { @JsonClass(generateAdapter = true)
    data class Information(
        @Json(name = "storedFcmToken") val storedFcmToken: Boolean
    )
}