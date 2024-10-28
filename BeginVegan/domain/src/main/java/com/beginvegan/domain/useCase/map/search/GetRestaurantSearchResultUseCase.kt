package com.beginvegan.domain.useCase.map.search

import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.domain.repository.map.RestaurantSearchResultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantSearchResultUseCase @Inject constructor(private val restaurantSearchResultRepository: RestaurantSearchResultRepository) {
    suspend operator fun invoke(
        page: Int,
        latitude: String,
        longitude: String,
        searchWord: String,
        filter: String
    ): Flow<List<VeganMapRestaurant>> =
        restaurantSearchResultRepository.getRestaurantSearchResult(page, latitude, longitude,searchWord,filter)
}