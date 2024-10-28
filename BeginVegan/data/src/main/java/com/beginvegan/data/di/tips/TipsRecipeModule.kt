package com.beginvegan.data.di.tips

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.tips.TipsRecipeDetailMapper
import com.beginvegan.data.mapper.tips.TipsRecipeMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.tips.TipsRecipeRemoteDataSource
import com.beginvegan.data.repository.remote.tips.TipsRecipeRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.tips.TipsRecipeRepositoryImpl
import com.beginvegan.data.retrofit.tips.TipsRecipeService
import com.beginvegan.domain.repository.tips.TipsRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class TipsRecipeModule {
    @Singleton
    @Provides
    fun provideTipsRecipeService(retrofit: Retrofit): TipsRecipeService {
        return retrofit.create(TipsRecipeService::class.java)
    }

    @Provides
    @Singleton
    fun provideTipsRecipeRemoteDataSource(
        tipsRecipeService: TipsRecipeService,
        authTokenDataSource: AuthTokenDataSource,
    ): TipsRecipeRemoteDataSource {
        return TipsRecipeRemoteDataSourceImpl(tipsRecipeService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideTipsRecipeRepository(
        tipsRecipeRemoteDataSource: TipsRecipeRemoteDataSource,
        tipsRecipeMapper: TipsRecipeMapper,
        tipsRecipeDetailMapper: TipsRecipeDetailMapper
    ): TipsRecipeRepository {
        return TipsRecipeRepositoryImpl(
            tipsRecipeRemoteDataSource,
            tipsRecipeMapper,
            tipsRecipeDetailMapper
        )
    }

    @Provides
    @Singleton
    fun provideTipsRecipeMapper(): TipsRecipeMapper {
        return TipsRecipeMapper()
    }

    @Provides
    @Singleton
    fun provideTipsRecipeDetailMapper(): TipsRecipeDetailMapper {
        return TipsRecipeDetailMapper()
    }
}