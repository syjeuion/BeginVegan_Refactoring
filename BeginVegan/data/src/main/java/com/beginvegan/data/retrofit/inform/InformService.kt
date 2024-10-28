package com.beginvegan.data.retrofit.inform

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.inform.InformModifyRestaurantRequest
import com.beginvegan.data.model.inform.InformNewRestaurantRequest
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface InformService {

    @POST("/api/v1/suggestions/registration")
    suspend fun postInformNewRestaurant(
        @Header("Authorization") token: String,
        @Body informNewRestaurantRequest: InformNewRestaurantRequest
    ): ApiResponse<BaseResponse>

    @POST("/api/v1/suggestions/modification")
    suspend fun postInformModifyRestaurant(
        @Header("Authorization") token: String,
        @Body informModifyRestaurantRequest: InformModifyRestaurantRequest
    ): ApiResponse<BaseResponse>
}