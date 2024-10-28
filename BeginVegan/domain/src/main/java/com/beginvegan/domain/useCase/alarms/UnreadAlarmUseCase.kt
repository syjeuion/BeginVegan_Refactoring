package com.beginvegan.domain.useCase.alarms

import com.beginvegan.domain.model.alarms.AlarmLists
import com.beginvegan.domain.repository.alarms.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnreadAlarmUseCase @Inject constructor(private val alarmRepository: AlarmRepository)  {
    suspend operator fun invoke(): Flow<Result<AlarmLists>> {
        return alarmRepository.getAlarms()
    }
}