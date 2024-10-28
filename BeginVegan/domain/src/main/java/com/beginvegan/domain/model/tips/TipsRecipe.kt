package com.beginvegan.domain.model.tips

data class TipsRecipeListItem(
    val id: Int,
    val name: String,
    val veganType: String,
    var isBookmarked: Boolean
)

data class TipsRecipeDetail(
    val id: Int,
    val name: String,
    val veganType: String,
    val ingredients: List<RecipeIngredient>,
    val blocks: List<RecipeBlock>,
    var isBookmarked: Boolean
)

data class RecipeIngredient(
    val id: Int,
    val name: String
)

data class RecipeBlock(
    val content: String,
    val sequence: Int
)

data class RecipeDetailPosition(
    val position:Int,
    val item:TipsRecipeListItem
)

data class CheckChange(
    val check: Boolean,
    val position: Int
)