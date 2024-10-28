package com.beginvegan.data.mapper.mypage

import com.beginvegan.data.model.mypage.MyReviewDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.mypage.MyReview

class MypageMyReviewMapper:Mapper<List<MyReviewDto>,List<MyReview>> {
    override fun mapFromEntity(type: List<MyReviewDto>): List<MyReview> {
        return type.map { MyReview(
            reviewId = it.reviewId,
            restaurantName = it.restaurantName,
            date = it.date,
            rate = it.rate,
            images = it.images,
            content = it.content,
            countRecommendation = it.countRecommendation,
            recommendation = it.recommendation
        ) }
    }
}