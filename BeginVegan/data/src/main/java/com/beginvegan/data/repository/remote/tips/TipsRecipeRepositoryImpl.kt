package com.beginvegan.data.repository.remote.tips

import com.beginvegan.data.mapper.tips.TipsRecipeDetailMapper
import com.beginvegan.data.mapper.tips.TipsRecipeMapper
import com.beginvegan.domain.model.tips.TipsRecipeDetail
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.domain.repository.tips.TipsRecipeRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.errorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class TipsRecipeRepositoryImpl @Inject constructor(
    private val tipsRecipeRemoteDataSource: TipsRecipeRemoteDataSource,
    private val tipsRecipeMapper: TipsRecipeMapper,
    private val tipsrecipeDetailMapper: TipsRecipeDetailMapper
) : TipsRecipeRepository {
    override suspend fun getRecipeList(page: Int): Flow<Result<List<TipsRecipeListItem>>> {
        return flow {
            try {
                val response = tipsRecipeRemoteDataSource.getRecipeList(page)
                when (response) {
                    is ApiResponse.Success -> {
                        val recipeList = tipsRecipeMapper.mapToRecipeList(response.data.information)
                        emit(Result.success(recipeList))
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getRecipeList error: ${response.errorBody}")
                        emit(Result.failure(Exception("getRecipeList Failed")))
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getRecipeList exception: ${response.message}")
                        emit(Result.failure(response.throwable))
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getRecipeList exception")
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun getRecipeDetail(id: Int): Result<TipsRecipeDetail> {
        return try {
            val response = tipsRecipeRemoteDataSource.getRecipeDetail(id)
            when (response) {
                is ApiResponse.Success -> {
                    val recipeDetail =
                        tipsrecipeDetailMapper.mapFromEntity(response.data.information)
                    Result.success(recipeDetail)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("getRecipeDetail error: ${response.errorBody}")
                    Result.failure(Exception("getRecipeDetail Failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("getRecipeDetail exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "getRecipeDetail exception")
            Result.failure(e)
        }
    }

    override suspend fun getRecipeForMe(
        page: Int
    ): Flow<Result<List<TipsRecipeListItem>>> {
        return flow{
            try {
                val response = tipsRecipeRemoteDataSource.getRecipeMy(page)
                when (response) {
                    is ApiResponse.Success -> {
                        val recipeList = tipsRecipeMapper.mapToRecipeList(response.data.information)
                        emit(Result.success(recipeList))
                    }

                    is ApiResponse.Failure.Error -> {
                        Timber.e("getRecipeForMe error: ${response.errorBody}")
                        emit(Result.failure(Exception("GetAlarms Failed")))
                    }

                    is ApiResponse.Failure.Exception -> {
                        Timber.e("getRecipeForMe exception: ${response.message}")
                        emit(Result.failure(response.throwable))
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "getRecipeForMe exception")
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun getHomeRecipe(): Result<List<TipsRecipeListItem>> {
        return try {
            val response = tipsRecipeRemoteDataSource.getHomeRecipe()
            when (response) {
                is ApiResponse.Success -> {
                    val recipeList = tipsRecipeMapper.mapToRecipeList(response.data.information)
                    Result.success(recipeList)
                }

                is ApiResponse.Failure.Error -> {
                    Timber.e("getHomeRecipe error: ${response.errorBody}")
                    Result.failure(Exception("getHomeRecipe Failed"))
                }

                is ApiResponse.Failure.Exception -> {
                    Timber.e("getHomeRecipe exception: ${response.message}")
                    Result.failure(response.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "getHomeRecipe exception")
            Result.failure(e)
        }
    }
}