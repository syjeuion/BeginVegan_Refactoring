package com.beginvegan.domain.useCase.inform

import com.beginvegan.domain.model.core.BasicResult
import com.beginvegan.domain.repository.inform.InformRepository
import javax.inject.Inject

class InformUseCase @Inject constructor(
    private val informRepository: InformRepository
) {
    suspend fun informNewRestaurant(
        name: String,
        location: String,
        content: String
    ): Result<BasicResult> = informRepository.informNewRestaurant(name, location, content)

    suspend fun informModifyRestaurant(restaurantId: Long, content: String): Result<BasicResult> =
        informRepository.informModifyRestaurant(restaurantId, content)
}