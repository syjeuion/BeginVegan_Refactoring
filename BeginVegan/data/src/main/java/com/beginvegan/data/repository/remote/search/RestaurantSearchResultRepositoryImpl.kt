package com.beginvegan.data.repository.remote.search

import com.beginvegan.data.mapper.map.VeganMapMapper
import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.domain.repository.map.RestaurantSearchResultRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class RestaurantSearchResultRepositoryImpl @Inject constructor(
    private val restaurantSearchResultDataSource: RestaurantSearchResultDataSource,
    private val veganMapMapper: VeganMapMapper
) : RestaurantSearchResultRepository {
    override suspend fun getRestaurantSearchResult(
        page: Int,
        latitude: String,
        longitude: String,
        searchWord: String,
        filter: String
    ): Flow<List<VeganMapRestaurant>> {
        return flow {
            try {
                val response =
                    restaurantSearchResultDataSource.getRestaurantSearchResult(
                        page, latitude, longitude, searchWord, filter
                    )
                when (response) {
                    is ApiResponse.Success -> {
                        Timber.e("getRestaurantSearchResult Success fetching restaurants: $response")
                        val restaurants = veganMapMapper.mapFromEntity(response.data.information)
                        emit(restaurants)
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getRestaurantSearchResult Error fetching restaurants: ${response.errorBody}")
                        emit(emptyList())
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getRestaurantSearchResult exception: ${response.message}")
                        emit(emptyList())
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getRestaurantSearchResult exception")
                emit(emptyList())
            }

        }
    }
}