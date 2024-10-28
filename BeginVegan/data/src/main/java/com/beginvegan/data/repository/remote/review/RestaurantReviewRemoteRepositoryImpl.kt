package com.beginvegan.data.repository.remote.review

import com.beginvegan.data.mapper.core.BaseMapper
import com.beginvegan.data.mapper.review.ReviewMapper
import com.beginvegan.domain.model.core.BasicResult
import com.beginvegan.domain.model.review.RestaurantReview
import com.beginvegan.domain.repository.review.RestaurantReviewRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class RestaurantReviewRemoteRepositoryImpl @Inject constructor(
    private val restaurantReviewRemoteDataSource: RestaurantReviewRemoteDataSource,
    private val baseMapper: BaseMapper,
    private val reviewMapper: ReviewMapper
) : RestaurantReviewRepository {
    override suspend fun getReviewByRestaurantId(
        reviewId: Long,
        page: Int,
        isPhoto: Boolean,
        filter: String
    ): Flow<List<RestaurantReview>> {
        return flow {
            try {
                val response =
                    restaurantReviewRemoteDataSource.getRestaurantReview(
                        reviewId,
                        page,
                        isPhoto,
                        filter
                    )
                when (response) {
                    is ApiResponse.Success -> {
                        Timber.e("getReviewByRestaurantId Success fetching restaurants: $response")
                        val restaurants = reviewMapper.mapFromEntity(response.data.information)
                        emit(restaurants)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getReviewByRestaurantId Error fetching restaurants: ${response.errorBody}")
                        emit(emptyList())
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getReviewByRestaurantId exception: ${response.message}")
                        emit(emptyList())
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getReviewByRestaurantId exception")
                emit(emptyList())
            }
        }
    }

    override suspend fun writeReview(
        reviewId: Long,
        content: String,
        rate: Double,
        files: List<String?>
    ): Result<BasicResult> {
        return try {
            val response =
                restaurantReviewRemoteDataSource.writeReview(reviewId, content, rate, files)
            when (response) {
                is ApiResponse.Success -> {
                    val result = baseMapper.mapFromEntity(response.data)
                    Result.success(result)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("writeReview error: ${response.errorBody}")
                    Result.failure(Exception("writeReview failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("writeReview exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "writeReview exception")
            Result.failure(e)
        }
    }

    override suspend fun reportReview(reviewId: Long, content: String): Result<BasicResult> {
        return try {
            val response =
                restaurantReviewRemoteDataSource.reportReview(reviewId, content)
            when (response) {
                is ApiResponse.Success -> {
                    val result = baseMapper.mapFromEntity(response.data)
                    Result.success(result)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("reportReview error: ${response.errorBody}")
                    Result.failure(Exception("reportReview failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("reportReview exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "reportReview exception")
            Result.failure(e)
        }
    }

    override suspend fun deleteReview(reviewId: Long): Result<BasicResult> {
        return try {
            val response =
                restaurantReviewRemoteDataSource.deleteReview(reviewId)
            when (response) {
                is ApiResponse.Success -> {
                    val result = baseMapper.mapFromEntity(response.data)
                    Result.success(result)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("deleteReview error: ${response.errorBody}")
                    Result.failure(Exception("deleteReview failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("deleteReview exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "deleteReview exception")
            Result.failure(e)
        }
    }
}