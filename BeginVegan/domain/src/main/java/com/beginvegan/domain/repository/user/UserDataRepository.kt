package com.beginvegan.domain.repository.user

import com.beginvegan.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    suspend fun setUserData(userProfile: UserProfile)
    fun getUserData(): Flow<UserProfile?>
}