package com.android.sample.bonial.di

import com.android.sample.bonial.ui.BrochureAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Singleton
    @Provides
    fun provideBrochureAdapter(): BrochureAdapter = BrochureAdapter()
}