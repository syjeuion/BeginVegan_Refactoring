package com.beginvegan.domain.model.mypage

data class MypageUserInfo(
    val id: Long,
    val imageUrl:String,
    val nickname:String,
    val userLevel:String,
    val veganType:String,
    val point:Int
)
