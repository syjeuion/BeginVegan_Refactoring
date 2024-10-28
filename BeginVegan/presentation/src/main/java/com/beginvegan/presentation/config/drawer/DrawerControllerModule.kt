package com.beginvegan.presentation.config.drawer

import android.app.Activity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.beginvegan.presentation.R
import com.beginvegan.presentation.util.DrawerController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import timber.log.Timber

@Module
@InstallIn(ActivityComponent::class)
object DrawerControllerModule {
    @Provides
    fun provideDrawerHandler(activity: Activity): DrawerController = object : DrawerController {
        override fun openDrawer() {
            Timber.d("open Drawer")
            val drawerLayout: DrawerLayout = activity.findViewById(R.id.dl_drawer)
            drawerLayout.openDrawer(GravityCompat.END)
        }
        override fun closeDrawer() {
            val drawerLayout: DrawerLayout = activity.findViewById(R.id.dl_drawer)
            drawerLayout.closeDrawer(GravityCompat.END)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

        override fun isDrawerOpen(): Boolean {
            val drawerLayout: DrawerLayout = activity.findViewById(R.id.dl_drawer)
            return drawerLayout.isDrawerOpen(GravityCompat.END)
        }
    }
}