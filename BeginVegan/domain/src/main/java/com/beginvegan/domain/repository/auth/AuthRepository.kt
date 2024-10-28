package com.beginvegan.domain.repository.auth

import com.beginvegan.domain.model.auth.AuthToken

/** 회원 로그인, 회원가입, 추가정보 입력 Repository
 *  구현 Impl -> Data Module */
interface AuthRepository {

//    suspend fun signUp(email: String, providerId: String): Result<Boolean>

    suspend fun signIn(email: String, providerId: String): Result<AuthToken>

    // Multipart
//    suspend fun signUpDetail(nick: String, veganType: String, )
}