package com.beginvegan.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY_TEST)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY_TEST)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        applicationClass = this
        // Firebase 초기화
        FirebaseApp.initializeApp(this)

        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            val channel = NotificationChannel(
                "channelId",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager?.createNotificationChannel(channel)
        }
        /**
         * 이부분 확인 부탁드립니다. 위 코드는 제가 추가한겁니다.
         */
        var keyHash = Utility.getKeyHash(this)
        Log.i("GlobalApplication", "$keyHash")
    }

    companion object{
        private lateinit var applicationClass: ApplicationClass
        fun getBeginVeganContext() = applicationClass
    }

    //    override fun onCreate() {
//        super.onCreate()
//        sSharedPreferences =
//            applicationContext.getSharedPreferences("BeginVegan", MODE_PRIVATE)
//        initRetrofitInstance()
//    }
//    companion object {
//        lateinit var sRetrofit: Retrofit
//        lateinit var sSharedPreferences: SharedPreferences
//
//    }
//    private fun initRetrofitInstance() {
//        val client: OkHttpClient = OkHttpClient.Builder()
//            .readTimeout(5000, TimeUnit.MILLISECONDS)
//            .connectTimeout(5000, TimeUnit.MILLISECONDS)
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            .addNetworkInterceptor(AccessTokenInterceptor()) // JWT 자동 헤더 전송
//            .build()
//        sRetrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

}