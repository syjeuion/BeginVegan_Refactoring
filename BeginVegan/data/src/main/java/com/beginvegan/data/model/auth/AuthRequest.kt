package com.beginvegan.data.model.auth

import com.squareup.moshi.Json

data class AuthRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "providerId")
    val providerId: String
)
