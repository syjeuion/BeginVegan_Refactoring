package com.beginvegan.domain.model.map


data class VeganMapRestaurant(
    val id: Long,
    val name: String,
    val type: String?,
    val distance: Double,
    val rate: Double?,
    val latitude: String,
    val longitude: String,
    val thumbnail: String
)