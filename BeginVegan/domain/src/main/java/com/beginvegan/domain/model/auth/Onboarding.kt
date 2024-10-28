package com.beginvegan.domain.model.auth



data class Onboarding (
    val nickName: String,
    val veganType: String,
    val isDefaultImage: Boolean
)