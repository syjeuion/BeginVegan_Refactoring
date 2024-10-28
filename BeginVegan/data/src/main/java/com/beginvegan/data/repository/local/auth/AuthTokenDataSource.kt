package com.beginvegan.data.repository.local.auth

import kotlinx.coroutines.flow.Flow

interface AuthTokenDataSource {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
}
