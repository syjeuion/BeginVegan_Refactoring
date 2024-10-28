package com.beginvegan.data.mapper.review

import com.beginvegan.data.model.review.Review
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.review.RestaurantReview
import com.beginvegan.domain.model.review.Reviewer

class ReviewMapper : Mapper<List<Review>, List<RestaurantReview>> {
    override fun mapFromEntity(type: List<Review>): List<RestaurantReview> {
        return type.map {
            RestaurantReview(
                reviewId = it.reviewId,
                user = it.user as Reviewer,
                reviewType = it.reviewType,
                imageUrl = it.imageUrl,
                rate = it.rate,
                date = it.date,
                content = it.content,
                visible = it.visible,
                recommendationCount = it.recommendationCount,
                recommendation = it.recommendation
            )
        }
    }
}