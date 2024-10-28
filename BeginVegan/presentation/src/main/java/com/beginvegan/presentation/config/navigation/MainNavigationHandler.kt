package com.beginvegan.presentation.config.navigation

import androidx.navigation.NavController

interface MainNavigationHandler {
    fun navigateToHome()
    fun navigateToMap()
    fun navigateToTips()
    fun navigateToMypage()

    fun navigateHomeToVeganTest()
    fun navigateTestToVeganTestResult()
    fun navigateTestResultToTips()

    fun navigateHomeToMagazineDetail()
    fun navigateHomeToMyMagazine()
    fun navigateHomeToMyRecipe()

    fun navigateTipsToMagazineDetail()

    fun navigateMypageToEditProfile()
    fun navigateMypageToMyReview()
    fun navigateMypageToMyRestaurant()
    fun navigateMypageToMyMagazine()
    fun navigateMypageToMyRecipe()
    fun navigateMypageToMySetting()

    fun navigateMyReviewToMap()
    fun navigateMyRestaurantToMap()
    fun navigateMyRecipeToTips()
    fun navigateMyMagazineToTips()
    fun navigateMyMagazineToMagazineDetail()

    fun navigateTipsMagazineToMyMagazine()
    fun navigateTipsMagazineDetailToMyMagazine()
    fun navigateTipsRecipeToMyRecipe()

    fun navController():NavController
}