package com.beginvegan.data.di.tips

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.tips.TipsMagazineDetailMapper
import com.beginvegan.data.mapper.tips.TipsMagazineMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.tips.TipsMagazineRemoteDataSource
import com.beginvegan.data.repository.remote.tips.TipsMagazineRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.tips.TipsMagazineRepositoryImpl
import com.beginvegan.data.retrofit.tips.TipsMagazineService
import com.beginvegan.domain.repository.tips.TipsMagazineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class TipsMagazineModule {
    @Singleton
    @Provides
    fun provideTipsMagazineService(retrofit: Retrofit): TipsMagazineService {
        return retrofit.create(TipsMagazineService::class.java)
    }

    @Provides
    @Singleton
    fun provideTipsMagazineDataSource(
        tipsMagazineService: TipsMagazineService,
        authTokenDataSource: AuthTokenDataSource
    ): TipsMagazineRemoteDataSource {
        return TipsMagazineRemoteDataSourceImpl(tipsMagazineService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideTipsMagazineRepository(
        tipsMagazineRemoteDataSource: TipsMagazineRemoteDataSource,
        authTokenDataSource: AuthTokenDataSource,
        tipsMagazineMapper: TipsMagazineMapper,
        tipsMagazineDetailMapper: TipsMagazineDetailMapper
    ): TipsMagazineRepository {
        return TipsMagazineRepositoryImpl(
            tipsMagazineRemoteDataSource,
            authTokenDataSource,
            tipsMagazineMapper,
            tipsMagazineDetailMapper
        )
    }

    @Provides
    @Singleton
    fun provideTipsMagazineMapper(): TipsMagazineMapper {
        return TipsMagazineMapper()
    }

    @Provides
    @Singleton
    fun provideTipsMagazineDetailMapper(): TipsMagazineDetailMapper {
        return TipsMagazineDetailMapper()
    }
}