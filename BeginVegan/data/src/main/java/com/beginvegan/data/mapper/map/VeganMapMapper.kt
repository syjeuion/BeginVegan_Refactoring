package com.beginvegan.data.mapper.map

import com.beginvegan.data.model.map.VeganMapRestaurantDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.map.VeganMapRestaurant
import java.math.RoundingMode

class VeganMapMapper : Mapper<List<VeganMapRestaurantDto>, List<VeganMapRestaurant>> {
    override fun mapFromEntity(type: List<VeganMapRestaurantDto>): List<VeganMapRestaurant> {
        return type.map {
            val formattedDistance =
                it.distance.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble()
            VeganMapRestaurant(
                id = it.restaurantId,
                name = it.restaurantName,
                rate = it.rate,
                type = it.restaurantType.toString(),
                distance = formattedDistance,
                latitude = it.latitude,
                longitude = it.longitude,
                thumbnail = it.thumbnail
            )
        }
    }
}