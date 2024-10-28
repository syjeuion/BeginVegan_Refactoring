package com.beginvegan.data.di.mypage

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.mypage.MypageUserInfoMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.mypage.MypageUserInfoRemoteDataSource
import com.beginvegan.data.repository.remote.mypage.MypageUserInfoRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.mypage.MypageUserInfoRepositoryImpl
import com.beginvegan.data.retrofit.mypage.MypageUserInfoService
import com.beginvegan.domain.repository.mypage.MypageUserInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class MypageUserInfoModule {
    @Singleton
    @Provides
    fun provideMypageUserInfoService(retrofit: Retrofit): MypageUserInfoService {
        return retrofit.create(MypageUserInfoService::class.java)
    }

    @Provides
    @Singleton
    fun provideMypageUserInfoDataSource(
        mypageUserInfoService: MypageUserInfoService,
        authTokenDataSource: AuthTokenDataSource
    ): MypageUserInfoRemoteDataSource {
        return MypageUserInfoRemoteDataSourceImpl(mypageUserInfoService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideMypageUserInfoRepository(
        mypageUserInfoRemoteDataSource: MypageUserInfoRemoteDataSource,
        mypageUserInfoMapper: MypageUserInfoMapper
    ): MypageUserInfoRepository {
        return MypageUserInfoRepositoryImpl(
            mypageUserInfoRemoteDataSource,
            mypageUserInfoMapper
        )
    }

    @Provides
    @Singleton
    fun provideMypageUserInfoMapper(): MypageUserInfoMapper {
        return MypageUserInfoMapper()
    }
}