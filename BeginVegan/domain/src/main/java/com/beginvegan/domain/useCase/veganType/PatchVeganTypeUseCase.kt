package com.beginvegan.domain.useCase.veganType

import com.beginvegan.domain.repository.veganType.VeganTypeRepository
import javax.inject.Inject

class PatchVeganTypeUseCase @Inject constructor(private val veganTypeRepository: VeganTypeRepository) {
    suspend operator fun invoke(type:String, veganType:String): Result<Boolean> = veganTypeRepository.patchVeganType(type, veganType)
}