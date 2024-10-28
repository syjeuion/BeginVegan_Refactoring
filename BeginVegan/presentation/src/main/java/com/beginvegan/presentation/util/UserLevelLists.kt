package com.beginvegan.presentation.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.beginvegan.presentation.R

data class UserLevelLists(
    val context:Context,
    val userLevelIllus:List<Drawable?> = listOf(
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_1_seed),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_2_cotyledon),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_3_sprout),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_4_stem),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_5_leaf),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_6_tree),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_7_flower),
        ContextCompat.getDrawable(context, R.drawable.illus_user_level_8_fruit)
    ),
    val userLevelIcons:List<Drawable?> = listOf(
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_1_seed),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_2_cotyledon),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_3_sprout),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_4_stem),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_5_leaf),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_6_tree),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_7_flower),
        ContextCompat.getDrawable(context, R.drawable.ic_user_level_8_fruit),
    ),
    val userLevelMaxPoint:List<Int> = listOf(
        2,5,10,20,30,50,100,100
    )
)
