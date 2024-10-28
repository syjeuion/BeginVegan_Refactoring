package com.beginvegan.presentation.di

import com.beginvegan.core_fcm.IntentProvider
import com.beginvegan.core_fcm.di.MAIN
import com.beginvegan.presentation.util.MainIntentProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainAcitivityModule {
    @Binds
    @MAIN
    @Singleton
    abstract fun bindsIntentProvider(intentProvider: MainIntentProvider):IntentProvider
}