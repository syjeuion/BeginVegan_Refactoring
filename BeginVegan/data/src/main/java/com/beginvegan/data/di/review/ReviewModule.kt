package com.beginvegan.data.di.review

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.di.core.network.NetworkModule
import com.beginvegan.data.mapper.core.BaseMapper
import com.beginvegan.data.mapper.review.ReviewMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.repository.remote.review.RestaurantReviewRemoteDataSource
import com.beginvegan.data.repository.remote.review.RestaurantReviewRemoteDataSourceImpl
import com.beginvegan.data.repository.remote.review.RestaurantReviewRemoteRepositoryImpl
import com.beginvegan.data.repository.remote.review.ReviewRecommendRemoteDataSource
import com.beginvegan.data.repository.remote.review.ReviewRecommendRepositoryImpl
import com.beginvegan.data.repository.remote.review.ReviewRecommnedRemoteDataSourceImpl
import com.beginvegan.data.retrofit.review.ReviewService
import com.beginvegan.domain.repository.review.RestaurantReviewRepository
import com.beginvegan.domain.repository.review.ReviewRecommendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class ReviewModule {
    @Singleton
    @Provides
    fun provideReviewService(retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewRecommendRemoteDataSource(
        reviewService: ReviewService,
        authTokenDataSource: AuthTokenDataSource
    ): ReviewRecommendRemoteDataSource {
        return ReviewRecommnedRemoteDataSourceImpl(reviewService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideReviewRecommendRepository(
        reviewRecommendRemoteDataSource: ReviewRecommendRemoteDataSource
    ): ReviewRecommendRepository {
        return ReviewRecommendRepositoryImpl(reviewRecommendRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideReviewRemoteDataSource(
        reviewService: ReviewService,
        authTokenDataSource: AuthTokenDataSource
    ): RestaurantReviewRemoteDataSource {
        return RestaurantReviewRemoteDataSourceImpl(reviewService, authTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideReviewRepository(
        restaurantReviewRemoteDataSource: RestaurantReviewRemoteDataSource,
        baseMapper: BaseMapper,
        reviewMapper: ReviewMapper
    ): RestaurantReviewRepository {
        return RestaurantReviewRemoteRepositoryImpl(
            restaurantReviewRemoteDataSource,
            baseMapper,
            reviewMapper
        )
    }


    @Provides
    fun provideReviewMapper(): ReviewMapper {
        return ReviewMapper()
    }

}