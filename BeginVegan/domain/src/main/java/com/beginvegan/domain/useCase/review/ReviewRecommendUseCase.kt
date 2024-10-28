package com.beginvegan.domain.useCase.review

import com.beginvegan.domain.repository.review.ReviewRecommendRepository
import javax.inject.Inject

class ReviewRecommendUseCase @Inject constructor(
    private val reviewRecommendRepository: ReviewRecommendRepository
){
    suspend fun updateReviewRecommend(reviewId:Int): Result<Boolean> =
        reviewRecommendRepository.updateReviewRecommend(reviewId = reviewId)
}