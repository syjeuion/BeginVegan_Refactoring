package com.beginvegan.data.model.map


enum class RestaurantType(val kr: String, val eng: String) {
    BAKERY("베이커리", "BAKERY"),
    CAFE("카페", "CAFE"),
    CHINESE("중식당", "CHINESE"),
    ETC("기타", "ETC"),
    WESTERN("양식", "WESTERN"),
    KOR("한식", "KOR"),
    JAPANESE("일식", "JAPANESE");

    companion object{
        fun getValues(): MutableList<RestaurantType> = RestaurantType.values().toMutableList()
        fun fromKr(value: String): RestaurantType? {
            return RestaurantType.values().find { it.kr == value }
        }

        fun fromEng(value: String): RestaurantType? {
            return RestaurantType.values().find { it.eng == value }
        }
        fun getKoreanNameFromEng(engName: String): String? {
            return fromEng(engName)?.kr
        }
    }

}