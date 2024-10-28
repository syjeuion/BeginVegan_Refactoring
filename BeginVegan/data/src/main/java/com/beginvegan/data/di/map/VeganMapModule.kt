package com.beginvegan.data.di.map

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.map.RecommendRestaurantMapper
import com.beginvegan.data.mapper.map.RestaurantDetailMapper
import com.beginvegan.data.mapper.map.VeganMapMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.map.VeganMapRemoteDataSource
import com.beginvegan.data.repository.remote.map.VeganMapRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.map.VeganMapRemoteRepositoryImpl
import com.beginvegan.data.repository.remote.search.RestaurantSearchResultDataSource
import com.beginvegan.data.repository.remote.search.RestaurantSearchResultDataSourceImpl
import com.beginvegan.data.repository.remote.search.RestaurantSearchResultRepositoryImpl
import com.beginvegan.data.retrofit.map.VeganMapService
import com.beginvegan.domain.repository.map.RestaurantSearchResultRepository
import com.beginvegan.domain.repository.map.VeganMapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class VeganMapModule {
    @Provides
    @Singleton
    fun provideVeganMapRestaurant(retrofit: Retrofit): VeganMapService {
        return retrofit.create(VeganMapService::class.java)
    }

    @Provides
    @Singleton
    fun provideVeganMapRemoteDataSource(
        veganMapService: VeganMapService,
        authTokenDataSource: AuthTokenDataSource
    ): VeganMapRemoteDataSource {
        return VeganMapRemoteDataSourceImpl(veganMapService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideVeganMapRepository(
        veganMapRemoteDataSource: VeganMapRemoteDataSource,
        veganMapMapper: VeganMapMapper,
        restaurantDetailMapper: RestaurantDetailMapper,
        recommendRestaurantMapper: RecommendRestaurantMapper
    ): VeganMapRepository {
        return VeganMapRemoteRepositoryImpl(
            veganMapRemoteDataSource,
            veganMapMapper,
            restaurantDetailMapper,
            recommendRestaurantMapper
        )
    }

    @Provides
    @Singleton
    fun provideRestaurantSearchResultDataSource(
        veganMapService: VeganMapService,
        authTokenDataSource: AuthTokenDataSource
    ): RestaurantSearchResultDataSource {
        return RestaurantSearchResultDataSourceImpl(veganMapService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideRestaurantSearchResultRepository(
        restaurantSearchResultDataSource: RestaurantSearchResultDataSource,
        veganMapMapper: VeganMapMapper,
    ): RestaurantSearchResultRepository {
        return RestaurantSearchResultRepositoryImpl(
            restaurantSearchResultDataSource,
            veganMapMapper,
        )
    }

    @Provides
    @Singleton
    fun provideVeganMapMapper(): VeganMapMapper {
        return VeganMapMapper()
    }

    @Provides
    @Singleton
    fun provideRestaurantDetailMapper(): RestaurantDetailMapper {
        return RestaurantDetailMapper()
    }

    @Provides
    @Singleton
    fun provideRecommendRestaurantMapper(): RecommendRestaurantMapper {
        return RecommendRestaurantMapper()
    }

}