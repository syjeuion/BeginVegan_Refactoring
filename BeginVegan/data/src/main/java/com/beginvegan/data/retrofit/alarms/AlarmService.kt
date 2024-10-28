package com.beginvegan.data.retrofit.alarms

import com.beginvegan.data.model.alarms.GetAlarmResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface AlarmService {

    @GET("/api/v1/alarms")
    suspend fun getAlarms(
        @Header("Authorization") token: String
    ): ApiResponse<GetAlarmResponse>

}