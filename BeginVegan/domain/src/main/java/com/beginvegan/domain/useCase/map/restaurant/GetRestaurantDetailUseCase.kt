package com.beginvegan.domain.useCase.map.restaurant

import com.beginvegan.domain.model.map.RestaurantDetail
import com.beginvegan.domain.repository.map.VeganMapRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantDetailUseCase @Inject constructor(
    private val veganMapRepository: VeganMapRepository
) {
    suspend operator fun invoke(
        restaurantId: Long,
        latitude: String,
        longitude: String
    ): Flow<RestaurantDetail> =
        veganMapRepository.getRestaurantDetail(
            restaurantId,
            latitude,
            longitude
        )
}
