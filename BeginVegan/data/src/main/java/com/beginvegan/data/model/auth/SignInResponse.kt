package com.beginvegan.data.model.auth

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SignInResponse (
    val check: Boolean,
    val information: AuthResponse
)
