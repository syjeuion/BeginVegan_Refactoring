package com.beginvegan.domain.repository.map

import com.beginvegan.domain.model.map.VeganMapRestaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantSearchResultRepository {
    suspend fun getRestaurantSearchResult(
        page: Int,
        latitude: String,
        longitude: String,
        searchWord: String,
        filter: String
    ): Flow<List<VeganMapRestaurant>>
}