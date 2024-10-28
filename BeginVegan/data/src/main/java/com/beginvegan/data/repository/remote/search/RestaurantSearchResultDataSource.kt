package com.beginvegan.data.repository.remote.search

import com.beginvegan.data.model.map.VeganMapRestaurantResponse
import com.skydoves.sandwich.ApiResponse

interface RestaurantSearchResultDataSource {
    suspend fun getRestaurantSearchResult(
        page: Int,
        latitude: String,
        longitude: String,
        searchWord: String,
        filter: String
    ): ApiResponse<VeganMapRestaurantResponse>
}