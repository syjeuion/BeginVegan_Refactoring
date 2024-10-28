package com.beginvegan.data.repository.remote.inform

import com.beginvegan.data.mapper.core.BaseMapper
import com.beginvegan.domain.model.core.BasicResult
import com.beginvegan.domain.repository.inform.InformRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import timber.log.Timber
import javax.inject.Inject

class InformRemoteRepositoryImpl @Inject constructor(
    private val informRemoteDataSource: InformRemoteDataSource,
    private val baseMapper: BaseMapper,
) : InformRepository {
    override suspend fun informNewRestaurant(
        name: String,
        location: String,
        content: String
    ): Result<BasicResult> {
        return try {
            val response =
                informRemoteDataSource.informNewRestaurant(name, location, content)
            when (response) {
                is ApiResponse.Success -> {
                    val result = baseMapper.mapFromEntity(response.data)
                    Result.success(result)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("informNewRestaurant error: ${response.errorBody}")
                    Result.failure(Exception("informNewRestaurant failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("informNewRestaurant exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "informNewRestaurant exception")
            Result.failure(e)
        }
    }

    override suspend fun informModifyRestaurant(
        restaurantId: Long,
        content: String
    ): Result<BasicResult> {
        return try {
            val response =
                informRemoteDataSource.informModifyRestaurant(restaurantId, content)
            when (response) {
                is ApiResponse.Success -> {
                    val result = baseMapper.mapFromEntity(response.data)
                    Result.success(result)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("informModifyRestaurant error: ${response.errorBody}")
                    Result.failure(Exception("informModifyRestaurant failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("informModifyRestaurant exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "informModifyRestaurant exception")
            Result.failure(e)
        }
    }
}