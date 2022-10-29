package com.android.sample.bonial

import com.android.sample.bonial.di.AppComponent
import com.android.sample.bonial.di.DaggerAppComponent
import timber.log.Timber

class Application : android.app.Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        component = DaggerAppComponent.factory().create(this)
    }
}