package com.beginvegan.data.retrofit.map

import com.beginvegan.data.model.map.RecommendRestaurantResponse
import com.beginvegan.data.model.map.RestaurantDetailResponse
import com.beginvegan.data.model.map.VeganMapRestaurantResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface VeganMapService {

    // 홈 화면 - 위치 권한 x, 랜덤 식당 3개 조희
    @GET("/api/v1/restaurants/random/{count}")
    suspend fun getNearRestaurantWithOutPermission(
        @Header("Authorization") token: String,
        @Path("count") count: Long
    ): ApiResponse<RecommendRestaurantResponse>

    // 홈 화면 - 위치 권한 o, 10km 이내 랜덤 식당 3개 조희
    @GET("/api/v1/restaurants/random/permission/{count}")
    suspend fun getNearRestaurantWithPermission(
        @Header("Authorization") token: String,
        @Path("count") count: Long,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): ApiResponse<RecommendRestaurantResponse>

    //    // Map 1depth - 식당 리스트 조회 : 가까운 순
    @GET("/api/v1/restaurants/around")
    suspend fun getNearRestaurantMap(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): ApiResponse<VeganMapRestaurantResponse>

    // 식당 상세 조회 (메뉴 포함)
    @GET("/api/v1/restaurants/{restaurant-id}")
    suspend fun getRestaurantDetail(
        @Header("Authorization") token: String,
        @Path("restaurant-id") restaurantId: Long,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): ApiResponse<RestaurantDetailResponse>

    @GET("/api/v1/restaurants/search")
    suspend fun getSearchRestaurant(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("searchWord") searchWord: String,
        @Query("filter") filter: String,
    ): ApiResponse<VeganMapRestaurantResponse>
}