package com.beginvegan.presentation.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.beginvegan.presentation.R
import com.beginvegan.presentation.databinding.IncludeToolbarPrimaryBinding
import timber.log.Timber

fun setContentToolbar(context: Context, view:IncludeToolbarPrimaryBinding, title:String){
    setToolbar(view, title,
        ContextCompat.getColor(context, R.color.color_primary),
        ContextCompat.getColor(context, R.color.color_white),
        isBackIcon = true,
        isNotiIcon = false
    )
}
fun setMagazineDetailToolbar(context: Context, view:IncludeToolbarPrimaryBinding, title:String){
    setToolbar(view, title,
        ContextCompat.getColor(context, R.color.color_white),
        ContextCompat.getColor(context, R.color.color_primary),
        isBackIcon = true,
        isNotiIcon = false
    )
}
fun setToolbar(view:IncludeToolbarPrimaryBinding,title:String, backColor:Int, contentColor:Int , isBackIcon:Boolean,isNotiIcon:Boolean ){
    with(view){
        //title
        tvTitle.text = title
        //color
        toolbar.setBackgroundColor(backColor)
        tvTitle.setTextColor(contentColor)
        ibBackUp.backgroundTintList = ColorStateList.valueOf(contentColor)
        ibNotification.backgroundTintList = ColorStateList.valueOf(contentColor)
        //button
        ibBackUp.isVisible = isBackIcon
        ibNotification.isVisible = isNotiIcon
    }
}

