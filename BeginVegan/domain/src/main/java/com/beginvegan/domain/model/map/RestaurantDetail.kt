package com.beginvegan.domain.model.map

// 빈 생성자 처리
data class RestaurantDetail(
    val restaurantId: Long = 0,
    val name: String = "",
    val restaurantType: String = "",
    val address: Address = Address(),
    val distance: Double = 0.0,
    val rate: Double? = null,
    val reviewCount: Int = 0,
    val contactNumber: String = "",
    val bookmark: Boolean = false,
    val menus: List<Menus> = emptyList()
)

data class Address(
    val province: String = "",
    val city: String = "",
    val roadName: String = "",
    val detailAddress: String? = null
)

data class Menus(
    val id: Int = 0,
    val name: String = ""
)
