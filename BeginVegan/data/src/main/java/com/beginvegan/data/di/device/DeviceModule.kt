package com.beginvegan.data.di.device

import com.beginvegan.data.repository.local.device.FirstRunDataSource
import com.beginvegan.data.repository.local.device.FirstRunDataSourceImpl
import com.beginvegan.data.repository.local.device.FirstRunRepositoryImpl
import com.beginvegan.data.room.FirstRunDao
import com.beginvegan.data.room.RoomDatabaseManager
import com.beginvegan.domain.repository.device.FirstRunRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DeviceModule {

    @Provides
    @Singleton
    fun provideFirstRunDao(database: RoomDatabaseManager): FirstRunDao = database.firstRunDao()

    @Provides
    @Singleton
    fun provideFirstRunDataSource(
        firstRunDao: FirstRunDao
    ): FirstRunDataSource {
        return FirstRunDataSourceImpl(firstRunDao)
    }

    @Provides
    @Singleton
    fun provideFirstRunRepository(
        firstRunDataSource: FirstRunDataSource
    ): FirstRunRepository {
        return FirstRunRepositoryImpl(firstRunDataSource)
    }
}