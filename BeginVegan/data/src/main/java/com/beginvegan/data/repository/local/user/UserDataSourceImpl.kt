package com.beginvegan.data.repository.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.beginvegan.data.model.user.UserData
import com.beginvegan.domain.model.UserLevels
import com.beginvegan.domain.model.VeganTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserDataSource {
    private object PreferencesKeys {
        val USER_NICK_NAME = stringPreferencesKey("user_nick_name")
        val USER_LEVEL = stringPreferencesKey("user_level")
        val USER_VEGAN_TYPE = stringPreferencesKey("user_vegan_type")
    }

    override suspend fun setUserData(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NICK_NAME] = userData.nickName
            preferences[PreferencesKeys.USER_LEVEL] = userData.userLevel.eng
            preferences[PreferencesKeys.USER_VEGAN_TYPE] = userData.veganType.eng
        }
    }


    override fun getUserData(): Flow<UserData?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                Timber.d("User Data EmptyPreferences")
                emit(emptyPreferences())
            } else {
                Timber.d("User Data exception $exception")
                throw exception
            }
        }
            .map { preferences ->
            val nickName = preferences[PreferencesKeys.USER_NICK_NAME]
            val userLevelEng = preferences[PreferencesKeys.USER_LEVEL]
            val veganTypeEng = preferences[PreferencesKeys.USER_VEGAN_TYPE]

            val userLevel = userLevelEng?.let { UserLevels.fromKr(it) }
            val veganType = veganTypeEng?.let { VeganTypes.fromKr(it) }

            if (nickName != null && userLevel != null && veganType != null) {
                UserData(nickName, userLevel, veganType)
            } else {
                Timber.d("UserData DataStore Null!")
                null
            }
        }
    }
}