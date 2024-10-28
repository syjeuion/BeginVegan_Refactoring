package com.beginvegan.data.repository.remote.tips

import com.beginvegan.data.model.tips.TipsRecipeDetailResponse
import com.beginvegan.data.model.tips.TipsRecipeListResponse
import com.beginvegan.data.repository.local.auth.AuthTokenDataSource
import com.beginvegan.data.retrofit.tips.TipsRecipeService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class TipsRecipeRemoteDataSourceImpl @Inject constructor(
    private val tipsRecipeService: TipsRecipeService,
    private val authTokenDataSource: AuthTokenDataSource
) : TipsRecipeRemoteDataSource {

    override suspend fun getRecipeList(page: Int): ApiResponse<TipsRecipeListResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsRecipeService.getRecipeList(authHeader, page).suspendOnSuccess {
            Timber.d("getRecipeList successful")
            ApiResponse.Success(data)
        }.suspendOnError {
            Timber.e("getRecipeList error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }

    override suspend fun getRecipeDetail(id: Int): ApiResponse<TipsRecipeDetailResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsRecipeService.getRecipeDetail(authHeader, id).suspendOnSuccess {
            Timber.d("getRecipeDetail successful")
            ApiResponse.Success(data)
        }.suspendOnError {
            Timber.e("getRecipeDetail error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }

    override suspend fun getRecipeMy(
        page: Int
    ): ApiResponse<TipsRecipeListResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsRecipeService.getRecipeMy(authHeader, page).suspendOnSuccess {
            Timber.d("getRecipeMy successful")
            ApiResponse.Success(data)
        }.suspendOnError {
            Timber.e("getRecipeMy error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }

    override suspend fun getHomeRecipe(): ApiResponse<TipsRecipeListResponse> {
        val accessToken = authTokenDataSource.accessToken.first()
        val authHeader = "Bearer $accessToken"
        return tipsRecipeService.getHomeRecipe(authHeader).suspendOnSuccess {
            Timber.d("getHomeRecipe successful")
            ApiResponse.Success(data)
        }.suspendOnError {
            Timber.e("getHomeRecipe error: ${this.errorBody}")
            ApiResponse.Failure.Error(this)
        }
    }

}