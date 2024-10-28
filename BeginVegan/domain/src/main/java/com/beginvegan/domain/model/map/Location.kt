package com.beginvegan.domain.model.map

data class Location(
    val latitude: String,
    val longitude: String
)


fun Location.getLatitudeAsDouble(): Double {
    return latitude.toDoubleOrNull() ?: throw NumberFormatException("Invalid latitude value")
}

fun Location.getLongitudeAsDouble(): Double {
    return longitude.toDoubleOrNull() ?: throw NumberFormatException("Invalid longitude value")
}
