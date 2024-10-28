package com.beginvegan.domain.useCase.auth

import com.beginvegan.domain.model.auth.AuthToken
import com.beginvegan.domain.repository.auth.AuthRepository
import javax.inject.Inject

/** 로그인 UseCase **/
class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, providerId: String): Result<AuthToken> =
        authRepository.signIn(email, providerId)
}