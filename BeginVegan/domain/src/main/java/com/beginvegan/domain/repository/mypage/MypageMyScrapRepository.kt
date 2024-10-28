package com.beginvegan.domain.repository.mypage

import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import kotlinx.coroutines.flow.Flow

interface MypageMyScrapRepository {
    suspend fun getMyMagazineList(page: Int): Flow<Result<List<MypageMyMagazineItem>>>
    suspend fun getMyRecipeList(page: Int): Flow<Result<List<TipsRecipeListItem>>>
    suspend fun getMyRestaurantList(page: Int, latitude: String, longitude: String): Flow<Result<List<MypageMyRestaurantItem>>>
    suspend fun getMyReviewList(page: Int): Flow<Result<List<MyReview>>>
}