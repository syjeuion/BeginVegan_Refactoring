package com.beginvegan.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beginvegan.data.model.device.FirstRunEntity

@Dao
interface FirstRunDao {

    @Query("SELECT isFirstRun = 1 FROM first_run WHERE id = 1 LIMIT 1")
    suspend fun isFirstRun(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstRun(firstRun: FirstRunEntity)
}