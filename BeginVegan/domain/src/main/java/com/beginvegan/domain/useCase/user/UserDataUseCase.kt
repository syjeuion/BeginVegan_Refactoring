package com.beginvegan.domain.useCase.user

import com.beginvegan.domain.model.UserProfile
import com.beginvegan.domain.repository.user.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
): UserDataRepository {
    override suspend fun setUserData(userProfile: UserProfile) {
        userDataRepository.setUserData(userProfile)
    }

    override fun getUserData(): Flow<UserProfile?> {
        return userDataRepository.getUserData()
    }

}