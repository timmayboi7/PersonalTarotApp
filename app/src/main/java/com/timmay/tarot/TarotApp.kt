package com.timmay.tarot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TarotApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TarotApp
            private set
    }
}
