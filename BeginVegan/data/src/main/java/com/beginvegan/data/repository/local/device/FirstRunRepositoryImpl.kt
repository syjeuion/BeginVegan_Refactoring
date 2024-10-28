package com.beginvegan.data.repository.local.device

import com.beginvegan.domain.repository.device.FirstRunRepository
import javax.inject.Inject

class FirstRunRepositoryImpl @Inject constructor(
    private val firstRunDataSource: FirstRunDataSource
) : FirstRunRepository {
    override suspend fun isCheckFirstRun(): Boolean {
        return firstRunDataSource.isFirstRun()
    }

    override suspend fun updateFirstRunRecord() {
        firstRunDataSource.setFirstRunCompleted()
    }

}