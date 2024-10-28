package com.beginvegan.data.di.inform

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.core.BaseMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.inform.InformRemoteDataSource
import com.beginvegan.data.repository.remote.inform.InformRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.inform.InformRemoteRepositoryImpl
import com.beginvegan.data.retrofit.inform.InformService
import com.beginvegan.domain.repository.inform.InformRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class InformModule {

    @Singleton
    @Provides
    fun provideInformService(retrofit: Retrofit): InformService {
        return retrofit.create(InformService::class.java)
    }

    @Provides
    @Singleton
    fun provideInformRemoteDataSource(
        informService: InformService,
        authTokenDataSource: AuthTokenDataSource
    ): InformRemoteDataSource {
        return InformRemoteDataSourceImpl(informService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideInformRepository(
        informRemoteDataSource: InformRemoteDataSource,
        baseMapper: BaseMapper
    ): InformRepository {
        return InformRemoteRepositoryImpl(informRemoteDataSource, baseMapper)
    }

}