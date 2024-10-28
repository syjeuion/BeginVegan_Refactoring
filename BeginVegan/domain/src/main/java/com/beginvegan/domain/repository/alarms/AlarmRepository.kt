package com.beginvegan.domain.repository.alarms

import com.beginvegan.domain.model.alarms.AlarmLists
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarms(): Flow<Result<AlarmLists>>
}