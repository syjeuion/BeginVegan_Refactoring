package com.beginvegan.data.repository.remote.mypage

import com.beginvegan.domain.repository.mypage.MypagePushRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import timber.log.Timber
import javax.inject.Inject

class MypagePushRepositoryImpl @Inject constructor(
    private val mypagePushRemoteDataSource: MypagePushRemoteDataSource
):MypagePushRepository {
    override suspend fun getPushState(): Result<Boolean> {
        return try {
            when (val response = mypagePushRemoteDataSource.getPushState()) {
                is ApiResponse.Success -> {
                    Result.success(response.data.information.alarmSetting)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("getPushState error: ${response.errorBody}")
                    Result.failure(Exception("getPushState failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("getPushState exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "getPushState exception")
            Result.failure(e)
        }
    }

    override suspend fun patchPush(): Result<Boolean> {
        return try {
            when (val response = mypagePushRemoteDataSource.patchPush()) {
                is ApiResponse.Success -> {
                    Result.success(response.data.information.alarmSetting)
                }
                is ApiResponse.Failure.Error -> {
                    Timber.e("patchPush error: ${response.errorBody}")
                    Result.failure(Exception("patchPush failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("patchPush exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "patchPush exception")
            Result.failure(e)
        }
    }
}