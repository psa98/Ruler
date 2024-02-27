package com.ponomarev.ruler

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


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