package com.beginvegan.domain.repository.tips

import com.beginvegan.domain.model.tips.TipsRecipeDetail
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import kotlinx.coroutines.flow.Flow

interface TipsRecipeRepository {
    suspend fun getRecipeList(page:Int): Flow<Result<List<TipsRecipeListItem>>>
    suspend fun getRecipeDetail(id:Int): Result<TipsRecipeDetail>
    suspend fun getRecipeForMe(page:Int): Flow<Result<List<TipsRecipeListItem>>>
    suspend fun getHomeRecipe(): Result<List<TipsRecipeListItem>>
}