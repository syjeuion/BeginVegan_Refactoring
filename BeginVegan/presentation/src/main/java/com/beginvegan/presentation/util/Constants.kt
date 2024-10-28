package com.beginvegan.presentation.util

object Constants {
    const val BASE_URL = "http://beginvegan.p-e.kr/"

    const val ACCESS_TOKEN = "ACCESS_TOKEN"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"

    const val PROVIDER_ID = "PROVIDER_ID"
    const val USER_EMAIL = "USER_EMAIL"
    const val USER_NAME = "USER_NAME"
    const val USER_IMG_URL = "USER_IMG_URL"
    const val USER_ID = "USER_ID"
    const val USER_MARKETING_CONSENT = "USER_MARKETING_CONSENT"
    const val USER_VEGAN_TYPE = "USER_VEGAN_TYPE"
    const val USER_PROVIDER = "USER_PROVIDER"
    const val USER_ROLE = "USER_ROLE"

    const val RESTAURANT_ID = "RESTAURANT_ID"
    const val RECOMMENDED_RESTAURANT = "RECOMMENDED_RESTAURANT"
    const val RECOMMENDED_POSITION = "RECOMMENDED_POSITION"

    const val TODAY_RECIPE ="TODAY_RECIPE"


    // VALIDATION
    const val EMAIL_VALIDATION =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val NICKNAME_VALIDATION = "^[가-힣]{1,8}$"
    const val PW_VALIDATION = """^[0-9a-zA-Z!@#$%^+\-=]*$"""

    // Permission const code
    const val ACCESS_FINE_LOCATION = 1000

    // Fragment request Key
    const val HOME_TODAY_RECIPE_TO_RECIPE = "HOME_TODAY_RECIPE_TO_RECIPE"
    const val HOME_RECOMMEND_RESTAURANT_TO_VEGAN_MAP = "HOME_RECOMMEND_RESTAURANT_TO_VEGAN_MAP"

    // Profile Default Image Path

}