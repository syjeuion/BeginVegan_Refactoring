package com.beginvegan.data.repository.remote.veganType

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.veganTest.VeganTypeRequest
import com.skydoves.sandwich.ApiResponse

interface VeganTypeRemoteDataSource {
    suspend fun patchVeganType(type:String, veganTypeRequest: VeganTypeRequest): ApiResponse<BaseResponse>
}