package com.beginvegan.data.retrofit.tips

import com.beginvegan.data.model.tips.TipsRecipeDetailResponse
import com.beginvegan.data.model.tips.TipsRecipeListResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TipsRecipeService {
    @GET("/api/v1/foods")
    suspend fun getRecipeList(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): ApiResponse<TipsRecipeListResponse>

    @GET("/api/v1/foods/{id}")
    suspend fun getRecipeDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ApiResponse<TipsRecipeDetailResponse>

    @GET("/api/v1/foods/my")
    suspend fun getRecipeMy(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): ApiResponse<TipsRecipeListResponse>

    @GET("/api/v1/foods/home/recipe")
    suspend fun getHomeRecipe(
        @Header("Authorization") token: String
    ): ApiResponse<TipsRecipeListResponse>
}