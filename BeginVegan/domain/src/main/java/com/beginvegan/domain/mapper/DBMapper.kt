package com.beginvegan.domain.mapper

interface DBMapper<E, D> {
    fun mapFromEntity(type: E): D
    fun entityFromMap(type: D): E
}