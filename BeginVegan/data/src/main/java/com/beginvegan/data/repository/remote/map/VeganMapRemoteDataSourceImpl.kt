package com.beginvegan.data.repository.remote.map

import com.beginvegan.data.model.map.RecommendRestaurantResponse
import com.beginvegan.data.model.map.RestaurantDetailResponse
import com.beginvegan.data.model.map.VeganMapRestaurantResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.map.VeganMapService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class VeganMapRemoteDataSourceImpl @Inject constructor(
    private val veganMapService: VeganMapService,
    private val authTokenDataSource: AuthTokenDataSource
) : VeganMapRemoteDataSource {

    override suspend fun getNearRestaurantMap(
        page: Int,
        latitude: String,
        longitude: String
    ): ApiResponse<VeganMapRestaurantResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return veganMapService.getNearRestaurantMap(authHeader, page, latitude, longitude)
            .suspendOnSuccess {
                Timber.d("getNearRestaurantMap successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("getNearRestaurantMap error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun getRestaurantDetail(
        restaurantId: Long,
        latitude: String,
        longitude: String
    ): ApiResponse<RestaurantDetailResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return veganMapService.getRestaurantDetail(authHeader, restaurantId, latitude, longitude)
            .suspendOnSuccess {
                Timber.d("getNearRestaurantMap successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("getNearRestaurantMap error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun getNearRestaurantWithPermission(
        count: Long,
        latitude: String,
        longitude: String
    ): ApiResponse<RecommendRestaurantResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return veganMapService.getNearRestaurantWithPermission(
            authHeader,
            count,
            latitude,
            longitude
        )
            .suspendOnSuccess {
                Timber.d("getNearRestaurantWithPermission successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("getNearRestaurantWithPermission error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun getNearRestaurantWithOutPermission(count: Long): ApiResponse<RecommendRestaurantResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return veganMapService.getNearRestaurantWithOutPermission(authHeader, count)
            .suspendOnSuccess {
                Timber.d("getNearRestaurantWithOutPermission successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("getNearRestaurantWithOutPermission error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }


}
