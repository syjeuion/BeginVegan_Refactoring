package com.beginvegan.data.repository.remote.inform

import com.beginvegan.data.model.core.BaseResponse
import com.skydoves.sandwich.ApiResponse

interface InformRemoteDataSource {
    suspend fun informNewRestaurant(name: String, location: String, content: String): ApiResponse<BaseResponse>
    suspend fun informModifyRestaurant(restaurantId: Long, content: String): ApiResponse<BaseResponse>
}