package com.beginvegan.data.repository.remote.auth

import com.beginvegan.data.model.auth.AuthRequest
import com.beginvegan.data.model.auth.SignInResponse
import com.beginvegan.data.retrofit.auth.UserService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.sandwich.suspendOnError
import timber.log.Timber
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : AuthRemoteDataSource {
//    override suspend fun signUp(authRequest: AuthRequest): ApiResponse<SignUpResponse> {
//        return userService.signUp(authRequest).suspendOnSuccess {
//            Timber.d("SignUp successful")
//            ApiResponse.Success(true)
//        }.suspendOnError {
//            Timber.e("SignUp error: ${this.errorBody}")
//            ApiResponse.Failure.Error(this)
//        }
//    }

    override suspend fun signIn(authRequest: AuthRequest): ApiResponse<SignInResponse> {
        return userService.signIn(authRequest).suspendOnSuccess {
            Timber.d("SignIn successful: ${this.data}")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("SignIn error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }
}
