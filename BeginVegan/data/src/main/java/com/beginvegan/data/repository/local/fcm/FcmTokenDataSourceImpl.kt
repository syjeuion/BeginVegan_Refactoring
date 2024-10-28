package com.beginvegan.data.repository.local.fcm

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class FcmTokenDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
):FcmTokenDataSource {
    companion object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
    }

    override suspend fun saveFcmToken(fcmToken: String) {
        dataStore.edit { preferences ->
            preferences[FCM_TOKEN] = fcmToken
        }
    }

    override val fcmToken: Flow<String?> = dataStore.data
        .catch { exception ->
            // 데이터 접근 중 발생한 예외 처리
            if (exception is IOException) {
                Timber.d("fcmToken EmptyPreferences")
                emit(emptyPreferences())
            } else {
                Timber.d("fcmToken exception $exception")
                throw exception
            }
        }.map { preferences ->
            preferences[FCM_TOKEN]
        }
}