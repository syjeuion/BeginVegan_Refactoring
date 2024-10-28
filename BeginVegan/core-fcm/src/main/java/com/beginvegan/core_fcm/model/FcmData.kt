package com.beginvegan.core_fcm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FcmData(
    val messageType: String?,
    val itemId: Int?,
    val alarmType: String?,
    val userLevel: String?
): Parcelable
