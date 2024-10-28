package com.beginvegan.domain.repository.map

import com.beginvegan.domain.model.map.HistorySearch
import kotlinx.coroutines.flow.Flow

interface HistorySearchRepository {

    suspend fun insertHistorySearch(description: String)

    suspend fun deleteHistorySearch(historySearch: HistorySearch)

    suspend fun getHistorySearch(): Flow<List<HistorySearch>>

    suspend fun deleteAllHistorySearch()
}