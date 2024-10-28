package com.beginvegan.data.repository.remote.map

import com.beginvegan.data.mapper.map.RecommendRestaurantMapper
import com.beginvegan.data.mapper.map.RestaurantDetailMapper
import com.beginvegan.data.mapper.map.VeganMapMapper
import com.beginvegan.domain.model.map.RecommendRestaurant
import com.beginvegan.domain.model.map.RestaurantDetail
import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.domain.repository.map.VeganMapRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class VeganMapRemoteRepositoryImpl @Inject constructor(
    private val veganMapRemoteDataSource: VeganMapRemoteDataSource,
    private val veganMapMapper: VeganMapMapper,
    private val restaurantDetailMapper: RestaurantDetailMapper,
    private val recommendRestaurantMapper: RecommendRestaurantMapper
) : VeganMapRepository {

    override suspend fun getNearRestaurantWithPermission(
        count: Long,
        latitude: String,
        longitude: String
    ): Flow<List<RecommendRestaurant>> {
        return flow {
            try {
                val response = veganMapRemoteDataSource.getNearRestaurantWithPermission(
                    count,
                    latitude,
                    longitude
                )
                when (response) {
                    is ApiResponse.Success -> {
                        Timber.e("getNearRestaurantWithPermission Success fetching restaurants: $response")
                        val restaurants =
                            recommendRestaurantMapper.mapFromEntity(response.data.information)
                        emit(restaurants)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getNearRestaurantWithPermission Error fetching restaurants: ${response.errorBody}")
                        emit(emptyList())
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getNearRestaurantWithPermission exception: ${response.message}")
                        emit(emptyList())
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getNearRestaurantWithPermission exception")
                emit(emptyList())
            }
        }
    }

    override suspend fun getNearRestaurantWithOutPermission(count: Long): Flow<List<RecommendRestaurant>> {
        return flow {
            try {
                val response = veganMapRemoteDataSource.getNearRestaurantWithOutPermission(count)
                when (response) {
                    is ApiResponse.Success -> {
                        Timber.e("getNearRestaurantWithOutPermission Success fetching restaurants: $response")
                        val restaurants =
                            recommendRestaurantMapper.mapFromEntity(response.data.information)
                        emit(restaurants)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getNearRestaurantWithOutPermission Error fetching restaurants: ${response.errorBody}")
                        emit(emptyList())
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getNearRestaurantWithOutPermission exception: ${response.message}")
                        emit(emptyList())
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getNearRestaurantWithOutPermission exception")
                emit(emptyList())
            }
        }
    }

    override suspend fun getNearRestaurantMap(
        page: Int,
        latitude: String,
        longitude: String
    ): Flow<List<VeganMapRestaurant>> {
        return flow {
            try {
                val response =
                    veganMapRemoteDataSource.getNearRestaurantMap(page, latitude, longitude)
                when (response) {
                    is ApiResponse.Success -> {
                        Timber.e("getNearRestaurantMap Success fetching restaurants: $response")
                        val restaurants = veganMapMapper.mapFromEntity(response.data.information)
                        emit(restaurants)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getNearRestaurantMap Error fetching restaurants: ${response.errorBody}")
                        emit(emptyList())
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getNearRestaurantMap exception: ${response.message}")
                        emit(emptyList())
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getNearRestaurantMap exception")
                emit(emptyList())
            }
        }
    }

    override suspend fun getRestaurantDetail(
        restaurantId: Long,
        latitude: String,
        longitude: String
    ): Flow<RestaurantDetail> {
        return flow {
            try {
                val response =
                    veganMapRemoteDataSource.getRestaurantDetail(restaurantId, latitude, longitude)
                when (response) {
                    is ApiResponse.Success -> {
                        Timber.e("getRestaurantDetail Success fetching restaurants: $response")
                        val restaurants =
                            restaurantDetailMapper.mapFromEntity(response.data.information)
                        emit(restaurants)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getRestaurantDetail Error fetching restaurants: ${response.errorBody}")
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getRestaurantDetail exception: ${response.message}")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getRestaurantDetail exception")
            }
        }
    }


}