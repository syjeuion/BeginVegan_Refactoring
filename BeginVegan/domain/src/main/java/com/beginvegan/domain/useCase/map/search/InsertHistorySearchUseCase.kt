package com.beginvegan.domain.useCase.map.search

import com.beginvegan.domain.repository.map.HistorySearchRepository
import javax.inject.Inject

class InsertHistorySearchUseCase @Inject constructor(private val historySearchRepository: HistorySearchRepository) {
    suspend operator fun invoke(description: String) = historySearchRepository.insertHistorySearch(description)
}