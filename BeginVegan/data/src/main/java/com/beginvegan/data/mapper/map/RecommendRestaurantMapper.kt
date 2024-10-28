package com.beginvegan.data.mapper.map

import com.beginvegan.data.model.map.RecommendRestaurantInformation
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.map.RecommendRestaurant

class RecommendRestaurantMapper :
    Mapper<List<RecommendRestaurantInformation>, List<RecommendRestaurant>> {
    override fun mapFromEntity(type: List<RecommendRestaurantInformation>): List<RecommendRestaurant> {
        return type.map {
            RecommendRestaurant(
                id = it.restaurantId,
                name = it.name,
                bookmark = it.bookmark,
                latitude = it.latitude,
                longitude = it.longitude,
                thumbnail = it.thumbnail
            )
        }
    }
}