package com.beginvegan.domain.useCase.map.restaurant

import com.beginvegan.domain.repository.map.VeganMapRepository
import javax.inject.Inject

class GetNearRestaurantUseCase @Inject constructor(
    private val veganMapRepository: VeganMapRepository
) {
    suspend fun getNearRestaurantWithPermission(
        count: Long,
        latitude: String,
        longitude: String
    ) =
        veganMapRepository.getNearRestaurantWithPermission(
            count,
            latitude,
            longitude
        )

    suspend fun getNearRestaurantWithOutPermission(
        count: Long
    ) =
        veganMapRepository.getNearRestaurantWithOutPermission(
            count
        )
}