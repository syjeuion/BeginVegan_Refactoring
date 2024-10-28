package com.beginvegan.domain.useCase.tips

import com.beginvegan.domain.model.tips.TipsMagazineDetail
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.domain.repository.tips.TipsMagazineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TipsMagazineUseCase @Inject constructor(private val tipsMagazineRepository: TipsMagazineRepository) {

    suspend fun getMagazineList(page: Int): Flow<Result<List<TipsMagazineItem>>> =
        tipsMagazineRepository.getMagazineList(page)

    suspend fun getMagazineDetail(id: Int): Result<TipsMagazineDetail> =
        tipsMagazineRepository.getMagazineDetail(id)

    suspend fun getHomeMagazine(): Result<List<TipsMagazineItem>> =
        tipsMagazineRepository.getHomeMagazine()
}