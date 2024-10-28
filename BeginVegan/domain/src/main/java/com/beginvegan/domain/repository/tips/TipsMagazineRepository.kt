package com.beginvegan.domain.repository.tips

import com.beginvegan.domain.model.tips.TipsMagazineDetail
import com.beginvegan.domain.model.tips.TipsMagazineItem
import kotlinx.coroutines.flow.Flow

interface TipsMagazineRepository {
    suspend fun getMagazineList(page: Int): Flow<Result<List<TipsMagazineItem>>>
    suspend fun getMagazineDetail(id: Int): Result<TipsMagazineDetail>
    suspend fun getHomeMagazine(): Result<List<TipsMagazineItem>>
}