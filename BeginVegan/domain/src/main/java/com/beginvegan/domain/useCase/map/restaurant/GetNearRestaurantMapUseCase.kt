package com.beginvegan.domain.useCase.map.restaurant

import com.beginvegan.domain.model.map.VeganMapRestaurant
import com.beginvegan.domain.repository.map.VeganMapRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNearRestaurantMapUseCase @Inject constructor(
    private val veganMapRepository: VeganMapRepository
) {
    suspend operator fun invoke(
        page: Int,
        latitude: String,
        longitude: String
    ): Flow<List<VeganMapRestaurant>> =
        veganMapRepository.getNearRestaurantMap(
            page,
            latitude,
            longitude
        )
}
