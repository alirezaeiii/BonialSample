package com.android.sample.bonial.di

import com.android.sample.bonial.repository.BrochureRepository
import com.android.sample.bonial.repository.BrochureRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindBrochureRepository(brochureRepository: BrochureRepositoryImpl): BrochureRepository
}