package com.beginvegan.domain.useCase.map.search

import com.beginvegan.domain.model.map.HistorySearch
import com.beginvegan.domain.repository.map.HistorySearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHistorySearchUseCase @Inject constructor(private val historySearchRepository: HistorySearchRepository) {
    suspend operator fun invoke(): Flow<List<HistorySearch>> =
        historySearchRepository.getHistorySearch()
}