package com.android.sample.bonial.di

import com.android.sample.bonial.ui.BrochureAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UtilsModule {

    @Singleton
    @Provides
    fun provideBrochureAdapter(): BrochureAdapter = BrochureAdapter()
}