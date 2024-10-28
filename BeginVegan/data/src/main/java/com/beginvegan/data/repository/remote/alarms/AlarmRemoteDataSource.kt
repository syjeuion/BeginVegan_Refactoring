package com.beginvegan.data.repository.remote.alarms

import com.beginvegan.data.model.alarms.GetAlarmResponse
import com.skydoves.sandwich.ApiResponse

interface AlarmRemoteDataSource {
    suspend fun getAlarms(): ApiResponse<GetAlarmResponse>
}