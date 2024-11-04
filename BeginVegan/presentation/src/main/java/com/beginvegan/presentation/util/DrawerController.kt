package com.beginvegan.presentation.util

import android.app.Activity

interface DrawerController {
    fun openDrawer()
    fun closeDrawer()
    fun isDrawerOpen():Boolean
}