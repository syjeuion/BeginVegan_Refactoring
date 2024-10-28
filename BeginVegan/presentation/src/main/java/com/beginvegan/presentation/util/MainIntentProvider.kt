package com.beginvegan.presentation.util

import android.content.Context
import android.content.Intent
import com.beginvegan.core_fcm.IntentProvider
import com.beginvegan.core_fcm.model.FcmData
import com.beginvegan.presentation.view.main.view.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainIntentProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : IntentProvider {
    override fun getMainActivityIntent(fcmData: FcmData): Intent =
        MainActivity.createIntent(context, fcmData)
}