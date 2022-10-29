package com.android.sample.bonial.di

import android.content.Context
import com.android.sample.bonial.ui.BrochureFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, DispatcherModule::class, NetworkModule::class,
        RepositoryModule::class, UtilsModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(brochureFragment: BrochureFragment)
}