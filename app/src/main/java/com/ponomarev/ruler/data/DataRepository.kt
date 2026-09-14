package com.ponomarev.ruler.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Repository handling global application settings.
 *
 * Improvement: Switched from 'object' to 'class' to allow for better Dependency Injection
 * and testability. Use [getInstance] for a singleton access pattern.
 */
class DataRepository private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Calibration parameter for the ruler.
     */
    var calParameter: Float = sharedPreferences.getFloat(KEY_CAL_PARAMETER, 1f)
        set(value) {
            if (field != value) {
                field = value
                sharedPreferences.edit { putFloat(KEY_CAL_PARAMETER, value) }
            }
        }

    /**
     * Whether the dark theme is enabled.
     */
    var darkTheme: Boolean = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
        set(value) {
            if (field != value) {
                field = value
                sharedPreferences.edit { putBoolean(KEY_DARK_THEME, value) }
            }
        }

    companion object {
        private const val PREFS_NAME = "global_settings"
        private const val KEY_CAL_PARAMETER = "cal_parameter"
        private const val KEY_DARK_THEME = "dark_theme"

        @Volatile
        private var INSTANCE: DataRepository? = null

        /**
         * Returns the singleton instance of [DataRepository].
         */
        fun getInstance(context: Context): DataRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
