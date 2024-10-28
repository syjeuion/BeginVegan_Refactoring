package com.beginvegan.data.model.core

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val check: Boolean,
    val information: ErrorInformation
)