package com.beginvegan.data.mapper.userInfo

import com.beginvegan.data.model.userInfo.HomeUserInfoDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.userInfo.HomeUserInfo

class HomeUserInfoMapper: Mapper<HomeUserInfoDto, HomeUserInfo> {
    override fun mapFromEntity(type: HomeUserInfoDto): HomeUserInfo = HomeUserInfo(
        nickName = type.nickname,
        userLevel = type.userLevel
    )

}