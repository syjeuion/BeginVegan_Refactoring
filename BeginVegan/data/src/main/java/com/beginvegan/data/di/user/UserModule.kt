package com.beginvegan.data.di.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.beginvegan.data.mapper.user.UserDataMapper
import com.beginvegan.data.repository.local.user.UserDataRepositoryImpl
import com.beginvegan.data.repository.local.user.UserDataSource
import com.beginvegan.data.repository.local.user.UserDataSourceImpl
import com.beginvegan.domain.repository.user.UserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDataSource(
        provideDataStore: DataStore<Preferences>
    ): UserDataSource {
        return UserDataSourceImpl(provideDataStore)
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(
        userDataSource: UserDataSource,
        userDataMapper: UserDataMapper
    ): UserDataRepository {
        return UserDataRepositoryImpl(userDataSource, userDataMapper)
    }

    @Provides
    @Singleton
    fun provideUserDataMapper(): UserDataMapper {
        return UserDataMapper()
    }
}
