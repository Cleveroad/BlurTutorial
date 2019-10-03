package com.cleveroad.blur_tutorial.sample

import android.app.Application

class SampleApp : Application() {

    companion object {
        lateinit var instance: SampleApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}