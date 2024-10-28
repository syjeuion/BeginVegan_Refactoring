package com.beginvegan.domain.repository.map

import com.beginvegan.domain.model.map.RecommendRestaurant
import com.beginvegan.domain.model.map.RestaurantDetail
import com.beginvegan.domain.model.map.VeganMapRestaurant
import kotlinx.coroutines.flow.Flow

interface VeganMapRepository {
    suspend fun getNearRestaurantWithPermission(
        count: Long,
        latitude: String,
        longitude: String
    ): Flow<List<RecommendRestaurant>>

    suspend fun getNearRestaurantWithOutPermission(
        count: Long,
    ): Flow<List<RecommendRestaurant>>

    suspend fun getNearRestaurantMap(
        page: Int,
        latitude: String,
        longitude: String
    ): Flow<List<VeganMapRestaurant>>

    suspend fun getRestaurantDetail(
        restaurant: Long,
        latitude: String,
        longitude: String
    ): Flow<RestaurantDetail>
}