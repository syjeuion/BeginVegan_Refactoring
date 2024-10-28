package com.beginvegan.data.model.tips

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TipsRecipeListResponse(
    val check: Boolean,
    val information: List<TipsRecipeListItemDto>
)

data class TipsRecipeListItemDto(
    @Json(name= "id") val id: Int,
    @Json(name= "name") val name: String,
    @Json(name= "veganType") val veganType: String,
    @Json(name= "isBookmarked") val isBookmarked: Boolean
)

data class TipsRecipeDetailDto(
    @Json(name= "id") val id: Int,
    @Json(name= "name") val name: String,
    @Json(name= "veganType") val veganType: String,
    @Json(name= "ingredients") val ingredients: List<RecipeIngredientDto>,
    @Json(name= "blocks") val blocks: List<RecipeBlockDto>,
    @Json(name= "isBookmarked") val isBookmarked: Boolean
)

data class RecipeIngredientDto(
    @Json(name= "id") val id: Int,
    @Json(name= "name") val name: String
)

data class RecipeBlockDto(
    @Json(name= "content") val content: String,
    @Json(name= "sequence") val sequence: Int
)
