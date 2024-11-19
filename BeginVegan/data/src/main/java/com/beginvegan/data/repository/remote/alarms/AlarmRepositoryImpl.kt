package com.beginvegan.data.repository.remote.alarms

import com.beginvegan.data.mapper.alarms.AlarmMapper
import com.beginvegan.domain.model.alarms.AlarmLists
import com.beginvegan.domain.repository.alarms.AlarmRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
    private val alarmMapper: AlarmMapper
):AlarmRepository{
    override suspend fun getAlarms(): Flow<Result<AlarmLists>> = flow {
        val response = alarmRemoteDataSource.getAlarms()
        when(response) {
            is ApiResponse.Success -> {
                val newAlarmList = alarmMapper.mapToAlarmLists(response.data.information)
                emit(Result.success(newAlarmList))
            }
            is ApiResponse.Failure.Error -> {
                Timber.e("GetAlarms error: ${response.errorBody}")
                emit(Result.failure(Exception("GetAlarms Failed")))
            }
            is ApiResponse.Failure.Exception -> {
                Timber.e("GetAlarms exception: ${response.message}")
                emit(Result.failure(response.throwable))
            }
        }
    }
}