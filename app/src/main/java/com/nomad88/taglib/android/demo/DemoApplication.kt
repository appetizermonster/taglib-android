package com.nomad88.taglib.android.demo

import android.app.Application
import com.airbnb.mvrx.Mavericks
import timber.log.Timber

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Mavericks.initialize(this)
    }
}
