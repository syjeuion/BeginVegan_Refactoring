package com.beginvegan.data.retrofit.tips

import com.beginvegan.data.model.tips.MagazineDetailResponse
import com.beginvegan.data.model.tips.MagazineResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TipsMagazineService {
    @GET("/api/v1/magazines/all")
    suspend fun getMagazineList(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): ApiResponse<MagazineResponse>

    @GET("/api/v1/magazines/{id}")
    suspend fun getMagazineDetail(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): ApiResponse<MagazineDetailResponse>

    @GET("api/v1/magazines/home/magazine")
    suspend fun getHomeMagazine(
        @Header("Authorization") token: String
    ): ApiResponse<MagazineResponse>
}