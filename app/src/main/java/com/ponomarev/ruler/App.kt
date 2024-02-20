package com.ponomarev.ruler

import android.app.Application
import android.content.Context

const val TAG: String = "Ruler"
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
    }
}