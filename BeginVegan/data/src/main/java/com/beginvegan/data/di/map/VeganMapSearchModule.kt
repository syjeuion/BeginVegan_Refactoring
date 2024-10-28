package com.beginvegan.data.di.map

import com.beginvegan.data.di.core.db.DataStoreModule
import com.beginvegan.data.mapper.map.HistorySearchMapper
import com.beginvegan.data.repository.local.search.HistorySearchLocalDataSource
import com.beginvegan.data.repository.local.search.HistorySearchLocalDataSourceImpl
import com.beginvegan.data.repository.local.search.HistorySearchRepositoryImpl
import com.beginvegan.data.room.HistorySearchDao
import com.beginvegan.data.room.RoomDatabaseManager
import com.beginvegan.domain.repository.map.HistorySearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [DataStoreModule::class])
@InstallIn(SingletonComponent::class)
class VeganMapSearchModule {
    @Provides
    @Singleton
    fun provideHistorySearchDao(database: RoomDatabaseManager): HistorySearchDao =
        database.historySearchDao()

    @Provides
    @Singleton
    fun provideHistorySearchRepository(
        historyDataSource: HistorySearchLocalDataSource,
        historySearchMapper: HistorySearchMapper
    ): HistorySearchRepository {
        return HistorySearchRepositoryImpl(historyDataSource, historySearchMapper)
    }

    @Provides
    @Singleton
    fun provideHistorySearchLocalDataSource(
        historySearchDao: HistorySearchDao
    ): HistorySearchLocalDataSource {
        return HistorySearchLocalDataSourceImpl(historySearchDao)
    }

    @Provides
    @Singleton
    fun provideHistorySearchMapper(): HistorySearchMapper {
        return HistorySearchMapper()
    }
}