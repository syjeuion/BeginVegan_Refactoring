package com.beginvegan.data.repository.local.auth

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

class AuthTokenDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AuthTokenDataSource {
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    override val accessToken: Flow<String?> = dataStore.data
        .catch { exception ->
            // 데이터 접근 중 발생한 예외 처리
            if (exception is IOException) {
                Timber.d("accessToken EmptyPreferences")
                emit(emptyPreferences())
            } else {
                Timber.d("accessToken exception $exception")
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN]
        }

    override val refreshToken: Flow<String?> = dataStore.data
        .catch { exception ->
            // 데이터 접근 중 발생한 예외 처리
            if (exception is IOException) {
                Timber.d("refreshToken EmptyPreferences")
                emit(emptyPreferences())
            } else {
                Timber.d("refreshToken exception $exception")
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN]
        }
}
