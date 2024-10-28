package com.beginvegan.data.model.auth

data class TokenRefreshResponse(
    val check: Boolean,
    val information: TokenResponse
)