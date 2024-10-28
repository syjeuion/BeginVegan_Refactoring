package com.beginvegan.data.repository.local.search

import com.beginvegan.data.model.map.HistorySearchEntity
import com.beginvegan.data.room.HistorySearchDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistorySearchLocalDataSourceImpl @Inject constructor(
    private val historySearchDao: HistorySearchDao
) : HistorySearchLocalDataSource {
    override suspend fun insertHistorySearch(historySearchEntity: HistorySearchEntity) {
        historySearchDao.insertHistory(historySearchEntity)
    }

    override suspend fun deleteHistorySearch(historySearchEntity: HistorySearchEntity) {
        historySearchDao.deleteHistory(historySearchEntity)
    }

    override suspend fun getHistorySearch(): Flow<List<HistorySearchEntity>> {
        return historySearchDao.getAllHistory()
    }

    override suspend fun deleteAllHistorySearch() {
        historySearchDao.deleteAllHistory()
    }

}