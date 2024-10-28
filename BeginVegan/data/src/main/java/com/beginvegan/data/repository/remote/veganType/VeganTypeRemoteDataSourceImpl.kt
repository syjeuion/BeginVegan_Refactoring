package com.beginvegan.data.repository.remote.veganType

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.veganTest.VeganTypeRequest
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.veganTypes.VeganTypeService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class VeganTypeRemoteDataSourceImpl @Inject constructor(
    private val veganTypeService: VeganTypeService,
    private val authTokenDataSource: AuthTokenDataSource
) : VeganTypeRemoteDataSource {

    override suspend fun patchVeganType(type:String, veganTypeRequest: VeganTypeRequest): ApiResponse<BaseResponse> {

        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"

        return veganTypeService.patchVeganType(authHeader,type, veganTypeRequest).suspendOnSuccess {
            Timber.d("PatchVeganType successful")
            ApiResponse.Success(true)
        }.suspendOnError {
            Timber.e("PatchVeganType error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }
}