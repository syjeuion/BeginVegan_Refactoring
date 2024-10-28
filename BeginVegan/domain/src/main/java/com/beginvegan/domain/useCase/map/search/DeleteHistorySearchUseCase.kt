package com.beginvegan.domain.useCase.map.search

import com.beginvegan.domain.model.map.HistorySearch
import com.beginvegan.domain.repository.map.HistorySearchRepository
import javax.inject.Inject

class DeleteHistorySearchUseCase @Inject constructor(private val historySearchRepository: HistorySearchRepository) {
    suspend operator fun invoke(historySearch: HistorySearch) =
        historySearchRepository.deleteHistorySearch(historySearch)
}