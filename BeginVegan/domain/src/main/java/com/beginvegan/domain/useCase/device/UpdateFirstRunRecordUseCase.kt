package com.beginvegan.domain.useCase.device

import com.beginvegan.domain.repository.device.FirstRunRepository
import javax.inject.Inject

class UpdateFirstRunRecordUseCase @Inject constructor(
    private val firstRunRepository: FirstRunRepository
){
    suspend operator fun invoke() {
        firstRunRepository.updateFirstRunRecord()
    }
}