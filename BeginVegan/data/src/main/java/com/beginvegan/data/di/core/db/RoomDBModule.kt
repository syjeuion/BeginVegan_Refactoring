package com.beginvegan.data.di.core.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.beginvegan.data.room.RoomDatabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDatabaseManager(
        @ApplicationContext context: Context,
        firstRunCallback: RoomDatabase.Callback
    ): RoomDatabaseManager = Room.databaseBuilder(
        context,
        RoomDatabaseManager::class.java,
        "beginvegan-database.db"
    )
        .fallbackToDestructiveMigration()
        .addCallback(firstRunCallback) // 콜백 추가
        .build()

    @Provides
    fun provideFirstRunCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("hilt","init first_run database insert")
                db.execSQL("INSERT INTO first_run (id, isFirstRun) VALUES(1, 1)")
            }
        }
    }
}