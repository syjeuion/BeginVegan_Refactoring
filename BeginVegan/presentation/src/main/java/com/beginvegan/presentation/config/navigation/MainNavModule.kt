package com.beginvegan.presentation.config.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.beginvegan.presentation.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import timber.log.Timber

@Module
@InstallIn(ActivityComponent::class)
object MainNavModule {

    @Provides
    fun provideNavController(activity: FragmentActivity): NavController {
        val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.fcw_main_container) as NavHostFragment
        Timber.d("navHostFragment: $navHostFragment")
        return navHostFragment.navController
    }

    @Provides
    fun provideMainNavigationHandler(navController: NavController): MainNavigationHandler {
        Timber.d("provideMainNavigationHandler")
        return MainNavigationImpl(navController)
    }
}