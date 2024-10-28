package com.beginvegan.data.repository.remote.veganType

import com.beginvegan.data.model.veganTest.VeganTypeRequest
import com.beginvegan.domain.repository.veganType.VeganTypeRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import timber.log.Timber
import javax.inject.Inject

class VeganTypeRepositoryImpl @Inject constructor(
    private val veganTypeRemoteDataSource: VeganTypeRemoteDataSource
):VeganTypeRepository {
    override suspend fun patchVeganType(type:String, veganType: String): Result<Boolean> {
        return try {
            val veganTypeRequest = VeganTypeRequest(veganType)
            val response = veganTypeRemoteDataSource.patchVeganType(type, veganTypeRequest)
            when (response) {
                is ApiResponse.Success -> Result.success(response.data.check)
                is ApiResponse.Failure.Error -> {
                    Timber.e("PatchVeganType error: ${response.errorBody}")
                    Result.failure(Exception("SignUp failed"))
                }
                is ApiResponse.Failure.Exception -> {
                    Timber.e("PatchVeganType exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "PatchVeganType exception")
            Result.failure(e)
        }
    }
}