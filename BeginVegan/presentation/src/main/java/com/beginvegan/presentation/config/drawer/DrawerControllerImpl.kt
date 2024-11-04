package com.beginvegan.presentation.config.drawer

import android.app.Activity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.beginvegan.presentation.R

class DrawerControllerImpl {
    companion object{
        fun openDrawer(activity: Activity) {
            val drawerLayout: DrawerLayout = activity.findViewById(R.id.dl_drawer)
            drawerLayout.openDrawer(GravityCompat.END)
        }

        fun closeDrawer(activity: Activity) {
            val drawerLayout: DrawerLayout = activity.findViewById(R.id.dl_drawer)
            drawerLayout.closeDrawer(GravityCompat.END)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

        fun isDrawerOpen(activity: Activity): Boolean {
            val drawerLayout: DrawerLayout = activity.findViewById(R.id.dl_drawer)
            return drawerLayout.isDrawerOpen(GravityCompat.END)
        }

    }
}