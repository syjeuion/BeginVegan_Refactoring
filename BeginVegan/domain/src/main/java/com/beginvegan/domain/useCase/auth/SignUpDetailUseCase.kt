//package com.example.domain.useCase.auth
//
//import com.example.domain.repository.auth.AuthRepository
//import javax.inject.Inject
//
///** 추가 정보 입력 UseCase **/
//class SignUpDetailUseCase @Inject constructor(private val authRepository: AuthRepository){
//
//    suspend operator fun invoke(email: String, providerId: String){
//        authRepository.signUp(email,providerId)
//    }
//}