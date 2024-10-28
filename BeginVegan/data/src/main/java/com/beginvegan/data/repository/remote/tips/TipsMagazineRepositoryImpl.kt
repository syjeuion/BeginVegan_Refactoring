package com.beginvegan.data.repository.remote.tips

import com.beginvegan.data.mapper.tips.TipsMagazineDetailMapper
import com.beginvegan.data.mapper.tips.TipsMagazineMapper
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.domain.model.tips.TipsMagazineDetail
import com.beginvegan.domain.model.tips.TipsMagazineItem
import com.beginvegan.domain.repository.tips.TipsMagazineRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class TipsMagazineRepositoryImpl @Inject constructor(
    private val tipsMagazineRemoteDataSource: TipsMagazineRemoteDataSource,
    private val authTokenDataSource: AuthTokenDataSource,
    private val tipsMagazineMapper: TipsMagazineMapper,
    private val tipsMagazineDetailMapper: TipsMagazineDetailMapper
) : TipsMagazineRepository {
    override suspend fun getMagazineList(
        page: Int
    ): Flow<Result<List<TipsMagazineItem>>> {
        return flow{
            try {
                val response = tipsMagazineRemoteDataSource.getMagazineList(page)
                when (response) {
                    is ApiResponse.Success -> {
                        val magazineList = tipsMagazineMapper.mapFromEntity(response.data.information)
                        emit(Result.success(magazineList))
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getMagazineList error: ${response.errorBody}")
                        emit(Result.failure(Exception("getMagazineList failed")))
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getMagazineList exception: ${response.message}")
                        emit(Result.failure(response.throwable))
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getMagazineList exception")
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun getMagazineDetail(
        id: Int
    ): Result<TipsMagazineDetail> {
        return try {
            val response = tipsMagazineRemoteDataSource.getMagazineDetail(id)
            when (response) {
                is ApiResponse.Success -> {
                    val magazineDetail =
                        tipsMagazineDetailMapper.mapFromEntity(response.data.information)
                    Result.success(magazineDetail)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("getMagazineDetail error: ${response.errorBody}")
                    Result.failure(Exception("getMagazineDetail failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("getMagazineDetail exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "getMagazineDetail exception")
            Result.failure(e)
        }
    }

    override suspend fun getHomeMagazine(): Result<List<TipsMagazineItem>> {
        return try {
            val response = tipsMagazineRemoteDataSource.getHomeMagazine()
            when (response) {
                is ApiResponse.Success -> {
                    val magazineList = tipsMagazineMapper.mapFromEntity(response.data.information)
                    Result.success(magazineList)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("getHomeMagazine error: ${response.errorBody}")
                    Result.failure(Exception("getHomeMagazine failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("getHomeMagazine exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "getHomeMagazine exception")
            Result.failure(e)
        }
    }

}