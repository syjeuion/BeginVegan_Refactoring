package com.beginvegan.data.mapper.auth

import com.beginvegan.data.model.auth.AuthResponse
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.auth.AuthToken

/** Token Mapper **
 * AccessToken = Token Type + " " + accessToken
 * RefreshToken = Token Type + " " + refreshToken
 * Mapper의 역할 [ Data Layer의 model (DTO) -> Domain Layer의 model로 변환 ]
 **/

class AuthMapper: Mapper<AuthResponse, AuthToken> {
    override fun mapFromEntity(type: AuthResponse): AuthToken {
        return AuthToken(
            accessToken = "${type.authRes?.tokenType} ${type.authRes?.accessToken}",
            refreshToken = "${type.authRes?.tokenType} ${type.authRes?.refreshToken}",
            additionalInfo = type.signUpCompleted!!
        )
    }
}