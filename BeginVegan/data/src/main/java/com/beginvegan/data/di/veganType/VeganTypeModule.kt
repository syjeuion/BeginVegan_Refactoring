package com.beginvegan.data.di.veganType

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.veganType.VeganTypeRemoteDataSource
import com.beginvegan.data.repository.remote.veganType.VeganTypeRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.veganType.VeganTypeRepositoryImpl
import com.beginvegan.data.retrofit.veganTypes.VeganTypeService
import com.beginvegan.domain.repository.veganType.VeganTypeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
object VeganTypeModule {
    @Singleton
    @Provides
    fun provideVeganTypeService(retrofit: Retrofit): VeganTypeService {
        return retrofit.create(VeganTypeService::class.java)
    }

    @Provides
    @Singleton
    fun provideVeganTypeRemoteDataSource(
        veganTypeService: VeganTypeService,
        authTokenDataSource: AuthTokenDataSource
    ): VeganTypeRemoteDataSource {
        return VeganTypeRemoteDataSourceImpl(veganTypeService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideVeganTypeRepository(
        veganTypeRemoteDataSource: VeganTypeRemoteDataSource,
    ): VeganTypeRepository {
        return VeganTypeRepositoryImpl(veganTypeRemoteDataSource)
    }
}