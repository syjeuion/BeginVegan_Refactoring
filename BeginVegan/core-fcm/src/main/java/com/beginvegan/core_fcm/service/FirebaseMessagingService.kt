package com.beginvegan.core_fcm.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.beginvegan.core_fcm.IntentProvider
import com.beginvegan.core_fcm.R
import com.beginvegan.core_fcm.di.MAIN
import com.beginvegan.core_fcm.model.FcmData
import com.beginvegan.core_fcm.useCase.FcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    @MAIN
    lateinit var intentProvider: IntentProvider

    @Inject
    lateinit var fcmTokenUseCase: FcmTokenUseCase

    private var title = ""
    private var body = ""

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("onNewToken 실행")
        Timber.d("FCM: onNewToken 새 토큰: $token")
        GlobalScope.launch(Dispatchers.IO) {
            fcmTokenUseCase.saveToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val fcmData = message.data.let {
            Timber.d("it[\"messageType\"]: ${it["messageType"]}")
            FcmData(
                messageType = it["messageType"],
                itemId = it["itemId"]?.toInt(),
                alarmType = it["alarmType"],
                userLevel = it["userLevel"]
            )
        }
        sendPushAlarm(
            title = title,
            body = body,
            fcmData = fcmData
        )
        if (message.data.isNotEmpty()) { //백그라운드
            sendPushAlarm(
                title = title,
                body = body,
                fcmData = fcmData
            )
        }

    }

    override fun handleIntent(intent: Intent?) {
        val newPushAlarmIntent = intent?.apply {
            val temp = extras?.apply {
                title = getString("gcm.notification.title").orEmpty()
                body = getString("gcm.notification.body").orEmpty()
            }
            replaceExtras(temp)
        }
        super.handleIntent(newPushAlarmIntent)
    }

    // FCM 푸시 메시지를 앱에서 알림
    private fun sendPushAlarm(title: String, body: String, fcmData:FcmData) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val notification = buildNotification(title, body, fcmData)
        notificationManager?.notify(1, notification)
    }

    private fun buildNotification(title: String, body: String, fcmData: FcmData): Notification {
        Timber.d("buildNotification: ${fcmData.alarmType}")
        val intent = intentProvider.getMainActivityIntent(fcmData)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, "channelId")
            .setSmallIcon(R.drawable.ic_fcm)
            .setContentTitle(title)
            .setContentText(body)
            .setContentTitle(URLDecoder.decode(title, "UTF-8"))
            .setContentText(URLDecoder.decode(body, "UTF-8"))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }

}