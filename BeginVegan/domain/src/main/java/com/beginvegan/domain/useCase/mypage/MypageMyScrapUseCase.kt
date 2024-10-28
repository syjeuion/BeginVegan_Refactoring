package com.beginvegan.domain.useCase.mypage

import com.beginvegan.domain.model.mypage.MyReview
import com.beginvegan.domain.model.mypage.MypageMyMagazineItem
import com.beginvegan.domain.model.mypage.MypageMyRestaurantItem
import com.beginvegan.domain.model.tips.TipsRecipeListItem
import com.beginvegan.domain.repository.mypage.MypageMyScrapRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MypageMyScrapUseCase @Inject constructor(
    private val mypageMyScrapRepository: MypageMyScrapRepository
){
    suspend fun getMyMagazineList(page: Int): Flow<Result<List<MypageMyMagazineItem>>> =
        mypageMyScrapRepository.getMyMagazineList(page)

    suspend fun getMyRecipeList(page:Int): Flow<Result<List<TipsRecipeListItem>>> =
        mypageMyScrapRepository.getMyRecipeList(page)

    suspend fun getMyRestaurantList(page: Int, latitude: String, longitude: String):Flow<Result<List<MypageMyRestaurantItem>>> =
        mypageMyScrapRepository.getMyRestaurantList(page,latitude,longitude)

    suspend fun getMyReviewList(page: Int): Flow<Result<List<MyReview>>> =
        mypageMyScrapRepository.getMyReviewList(page)
}