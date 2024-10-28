package com.beginvegan.data.repository.remote.inform

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.data.model.inform.InformModifyRestaurantRequest
import com.beginvegan.data.model.inform.InformNewRestaurantRequest
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.inform.InformService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class InformRemoteDataSourceImpl @Inject constructor(
    private val informService: InformService,
    private val authTokenDataSource: AuthTokenDataSource
) : InformRemoteDataSource {
    override suspend fun informNewRestaurant(
        name: String,
        location: String,
        content: String
    ): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"

        val body = InformNewRestaurantRequest(name, location, content)
        return informService.postInformNewRestaurant(authHeader, body)
            .suspendOnSuccess {
                Timber.d("informNewRestaurant successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("informNewRestaurant error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }

    override suspend fun informModifyRestaurant(
        restaurantId: Long,
        content: String
    ): ApiResponse<BaseResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"

        val body = InformModifyRestaurantRequest(restaurantId, content)
        return informService.postInformModifyRestaurant(authHeader, body)
            .suspendOnSuccess {
                Timber.d("informModifyRestaurant successful${this.data}")
                ApiResponse.Success(this.data)
            }.suspendOnError {
                Timber.e("informModifyRestaurant error: ${this.errorBody}")
                ApiResponse.Failure.Error(this)
            }
    }
}