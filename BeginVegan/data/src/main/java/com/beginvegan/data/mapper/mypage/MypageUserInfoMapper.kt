package com.beginvegan.data.mapper.mypage

import com.beginvegan.data.model.mypage.MypageUserInfoDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.mypage.MypageUserInfo

class MypageUserInfoMapper:Mapper<MypageUserInfoDto, MypageUserInfo> {
    override fun mapFromEntity(type: MypageUserInfoDto): MypageUserInfo {
        return MypageUserInfo(
            id = type.id,
            imageUrl = type.imageUrl,
            nickname = type.nickname,
            userLevel = type.userLevel,
            veganType = type.veganType,
            point = type.point
        )
    }
}