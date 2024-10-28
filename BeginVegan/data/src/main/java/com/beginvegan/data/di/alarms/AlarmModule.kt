package com.beginvegan.data.di.alarms

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.alarms.AlarmMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.alarms.AlarmRemoteDataSource
import com.beginvegan.data.repository.remote.alarms.AlarmRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.alarms.AlarmRepositoryImpl
import com.beginvegan.data.retrofit.alarms.AlarmService
import com.beginvegan.domain.repository.alarms.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class AlarmModule {
    @Singleton
    @Provides
    fun provideAlarmService(retrofit: Retrofit): AlarmService {
        return retrofit.create(AlarmService::class.java)
    }

    @Provides
    @Singleton
    fun provideAlarmRemoteDataSource(
        alarmService: AlarmService,
        authTokenDataSource: AuthTokenDataSource
    ): AlarmRemoteDataSource {
        return AlarmRemoteDataSourceImpl(alarmService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(
        alarmRemoteDataSource: AlarmRemoteDataSource,
        alarmMapper: AlarmMapper
    ): AlarmRepository {
        return AlarmRepositoryImpl(alarmRemoteDataSource, alarmMapper)
    }

    @Provides
    @Singleton
    fun provideAlarmMapper(): AlarmMapper {
        return AlarmMapper()
    }
}