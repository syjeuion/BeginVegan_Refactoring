package com.beginvegan.data.repository.local.user

import com.beginvegan.data.model.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    suspend fun setUserData(userData: UserData)
    fun getUserData(): Flow<UserData?>
}