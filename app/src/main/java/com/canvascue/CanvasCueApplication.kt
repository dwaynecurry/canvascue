package com.canvascue

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CanvasCueApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Any application-wide initialization can go here
    }
}