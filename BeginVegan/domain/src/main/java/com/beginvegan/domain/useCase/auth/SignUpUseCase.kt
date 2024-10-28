//package com.example.domain.useCase.auth
//
//import com.example.domain.repository.auth.AuthRepository
//import javax.inject.Inject
//
///** 회원가입 UseCase **/
//class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
//    suspend operator fun invoke(email: String, providerId: String): Result<Boolean> =
//        authRepository.signUp(email, providerId)
//}