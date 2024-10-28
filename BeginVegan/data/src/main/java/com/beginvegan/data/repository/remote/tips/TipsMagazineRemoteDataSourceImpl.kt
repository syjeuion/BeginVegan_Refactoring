package com.beginvegan.data.repository.remote.tips

import com.beginvegan.data.model.tips.MagazineDetailResponse
import com.beginvegan.data.model.tips.MagazineResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.tips.TipsMagazineService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class TipsMagazineRemoteDataSourceImpl @Inject constructor(
    private val tipsMagazineService: TipsMagazineService,
    private val authTokenDataSource: AuthTokenDataSource,
) : TipsMagazineRemoteDataSource {
    override suspend fun getMagazineList(
        page: Int
    ): ApiResponse<MagazineResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsMagazineService.getMagazineList(authHeader, page).suspendOnSuccess {
            Timber.d("GetMagazineList successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("GetMagazineList error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }

    override suspend fun getMagazineDetail(
        id: Int
    ): ApiResponse<MagazineDetailResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsMagazineService.getMagazineDetail(authHeader, id).suspendOnSuccess {
            Timber.d("GetMagazineDetail successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("GetMagazineDetail error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }

    override suspend fun getHomeMagazine(): ApiResponse<MagazineResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsMagazineService.getHomeMagazine(authHeader).suspendOnSuccess {
            Timber.d("GetHomeMagazine successful")
            ApiResponse.Success(this.data)
        }.suspendOnError {
            Timber.e("GetHomeMagazine error: ${this.errorBody}")
            ApiResponse.Failure.Error(this.errorBody)
        }
    }

}