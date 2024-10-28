package com.beginvegan.data.repository.remote.auth

import com.beginvegan.data.model.auth.AuthRequest
import com.beginvegan.data.model.auth.SignInResponse
import com.skydoves.sandwich.ApiResponse

/** RemoteDataSource
 *  네트워크 통신으로부터 데이터를 가져오는 DataSource 클래스
 *  **/
interface AuthRemoteDataSource {
//    suspend fun signUp(authRequest: AuthRequest): ApiResponse<SignUpResponse>
    suspend fun signIn(authRequest: AuthRequest): ApiResponse<SignInResponse>
}