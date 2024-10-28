package com.beginvegan.data.repository.remote.mypage

import com.beginvegan.data.model.mypage.MyMagazineResponse
import com.beginvegan.data.model.mypage.MyRecipeResponse
import com.beginvegan.data.model.mypage.MyRestaurantResponse
import com.beginvegan.data.model.mypage.MyReviewResponse
import com.skydoves.sandwich.ApiResponse

interface MypageMyScrapRemoteDataSource {
    suspend fun getMyMagazineList(page: Int): ApiResponse<MyMagazineResponse>
    suspend fun getMyRecipeList(page: Int): ApiResponse<MyRecipeResponse>
    suspend fun getMyRestaurantList(page: Int,latitude: String,longitude: String): ApiResponse<MyRestaurantResponse>
    suspend fun getMyReviewList(page: Int):ApiResponse<MyReviewResponse>
}