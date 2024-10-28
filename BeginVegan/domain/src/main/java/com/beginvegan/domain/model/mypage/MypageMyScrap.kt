package com.beginvegan.domain.model.mypage

data class MypageMyMagazineItem(
    val magazineId: Int,
    val title: String,
    val thumbnail: String,
    val editor: String,
    val writeTime: String
)

//data class MypageMyRecipeItem(
//    val foodId:Int,
//    val name:String,
//    val veganType:String
//)

data class MypageMyRestaurantItem(
    val restaurantId:Int,
    val thumbnail:String,
    val name:String,
    val restaurantType:String,
    val address:List<RestaurantAddress>
)
data class RestaurantAddress(
    val province:String,
    val city:String,
    val roadName:String,
    val detailAddress:String
)