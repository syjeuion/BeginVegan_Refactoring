package com.beginvegan.core_fcm

import android.content.Intent
import com.beginvegan.core_fcm.model.FcmData

interface IntentProvider {
    fun getMainActivityIntent(fcmData: FcmData):Intent
}