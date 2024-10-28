package com.beginvegan.data.mapper.map

import com.beginvegan.data.model.map.RestaurantInformation
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.map.Address
import com.beginvegan.domain.model.map.RestaurantDetail
import com.beginvegan.domain.model.map.Menus

class RestaurantDetailMapper : Mapper<RestaurantInformation, RestaurantDetail> {
    override fun mapFromEntity(type: RestaurantInformation): RestaurantDetail {
        return RestaurantDetail(
            restaurantId = type.restaurant.restaurantId,
            name = type.restaurant.name,
            restaurantType = type.restaurant.restaurantType,
            address = Address(
                province = type.restaurant.address.province,
                city = type.restaurant.address.city,
                roadName = type.restaurant.address.roadName,
                detailAddress = type.restaurant.address.detailAddress
            ),
            distance = type.restaurant.distance,
            rate = type.restaurant.rate,
            reviewCount = type.restaurant.reviewCount,
            contactNumber = type.restaurant.contactNumber,
            bookmark = type.restaurant.bookmark,
            menus = type.menus.map { menu ->
                Menus(
                    id = menu.id,
                    name = menu.name
                )
            }
        )
    }
}
