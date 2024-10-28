package com.beginvegan.data.model.user

import com.beginvegan.domain.model.UserLevels
import com.beginvegan.domain.model.VeganTypes

data class UserData(
    val nickName: String,
    val userLevel: UserLevels,
    val veganType: VeganTypes
)
