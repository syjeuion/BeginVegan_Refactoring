package com.beginvegan.domain.useCase.map.search

import com.beginvegan.domain.repository.map.HistorySearchRepository
import javax.inject.Inject

class DeleteAllHistorySearchUseCase @Inject constructor(private val historySearchRepository: HistorySearchRepository) {
    suspend operator fun invoke() =
        historySearchRepository.deleteAllHistorySearch()
}