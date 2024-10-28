package com.beginvegan.data.mapper.user

import com.beginvegan.data.model.user.UserData
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.UserProfile

class UserDataMapper : Mapper<UserData, UserProfile> {
    override fun mapFromEntity(type: UserData): UserProfile {
        return UserProfile(
            nickName = type.nickName,
            userLevel = type.userLevel,
            veganType = type.veganType
        )
    }
    fun mapToEntity(type: UserProfile): UserData {
        return UserData(
            nickName = type.nickName,
            userLevel = type.userLevel,
            veganType = type.veganType
        )
    }
}