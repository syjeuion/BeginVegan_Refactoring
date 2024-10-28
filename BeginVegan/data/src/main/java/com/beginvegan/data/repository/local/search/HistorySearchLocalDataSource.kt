package com.beginvegan.data.repository.local.search

import com.beginvegan.data.model.map.HistorySearchEntity
import kotlinx.coroutines.flow.Flow

interface HistorySearchLocalDataSource{
    suspend fun insertHistorySearch(historySearchEntity: HistorySearchEntity)
    suspend fun deleteHistorySearch(historySearchEntity: HistorySearchEntity)
    suspend fun getHistorySearch(): Flow<List<HistorySearchEntity>>
    suspend fun deleteAllHistorySearch()
}