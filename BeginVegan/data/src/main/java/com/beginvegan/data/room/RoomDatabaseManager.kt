package com.beginvegan.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.beginvegan.data.model.device.FirstRunEntity
import com.beginvegan.data.model.map.HistorySearchEntity

@Database(
    entities = [HistorySearchEntity::class, FirstRunEntity::class],
    version = 11,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class RoomDatabaseManager : RoomDatabase() {
    abstract fun historySearchDao(): HistorySearchDao
    abstract fun firstRunDao(): FirstRunDao
}
