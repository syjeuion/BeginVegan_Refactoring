package com.beginvegan.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beginvegan.data.model.map.HistorySearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorySearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historySearch: HistorySearchEntity)

    @Delete
    suspend fun deleteHistory(historySearch: HistorySearchEntity)

    @Query("SELECT * FROM historySearch")
    fun getAllHistory(): Flow<List<HistorySearchEntity>>

    @Query("DELETE FROM historySearch")
    suspend fun deleteAllHistory()

}