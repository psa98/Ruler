package com.ponomarev.ruler.data

import com.ponomarev.ruler.App


object DataRepository {

    private val sharedPreferences = App.appContext
        .getSharedPreferences("globalSettings", android.content.Context.MODE_PRIVATE)

    var calParameter: Float = sharedPreferences.getFloat("calParameter", 1f)
        set(value) {
            field = value
            sharedPreferences.edit().putFloat("calParameter", value).apply()
        }

    var darkTheme: Boolean = sharedPreferences.getBoolean("darkTheme", false)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean("darkTheme", value).apply()
        }
}