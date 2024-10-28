package com.beginvegan.domain.repository.veganType

interface VeganTypeRepository {
    suspend fun patchVeganType(type:String, veganType: String): Result<Boolean>
}