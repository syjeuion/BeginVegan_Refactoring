package com.beginvegan.data.di.mypage

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.mypage.MypagePushRemoteDataSource
import com.beginvegan.data.repository.remote.mypage.MypagePushRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.mypage.MypagePushRepositoryImpl
import com.beginvegan.data.retrofit.mypage.MypagePushService
import com.beginvegan.domain.repository.mypage.MypagePushRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class MypagePushModule {
    @Singleton
    @Provides
    fun provideMypagePushService(retrofit: Retrofit): MypagePushService {
        return retrofit.create(MypagePushService::class.java)
    }

    @Provides
    @Singleton
    fun provideMypagePushDataSource(
        mypagePushService: MypagePushService,
        authTokenDataSource: AuthTokenDataSource
    ): MypagePushRemoteDataSource {
        return MypagePushRemoteDataSourceImpl(mypagePushService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideMypagePushRepository(
        mypagePushRemoteDataSource: MypagePushRemoteDataSource
    ): MypagePushRepository {
        return MypagePushRepositoryImpl(mypagePushRemoteDataSource)
    }
}