package com.beginvegan.data.model.core

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorInformation(
    val timestamp: String,
    val message: String,
    val code: String?,
    val status: Int,
    @Json(name = "class") val className: String?,
    val errors: List<Any>
)