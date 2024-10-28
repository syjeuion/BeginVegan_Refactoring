package com.beginvegan.domain.model.map

data class RecommendRestaurant(
    val id: Long,
    val name: String,
    val bookmark: Boolean,
    val latitude: String,
    val longitude: String,
    val thumbnail: String
)