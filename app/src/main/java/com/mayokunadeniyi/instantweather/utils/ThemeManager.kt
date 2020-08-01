package com.mayokunadeniyi.instantweather.utils

import androidx.appcompat.app.AppCompatDelegate

/**
 * Created by Mayokun Adeniyi on 02/05/2020.
 */
object ThemeManager {
    private const val LIGHT_MODE = "Light"
    private const val DARK_MODE = "Dark"
    private const val AUTO_BATTERY_MODE = "Auto-battery"
    private const val FOLLOW_SYSTEM_MODE = "System"

    /**
     * This function helps persist the theme set by the user by getting the [themePreference] on initial startup
     * of the application.
     */
    fun applyTheme(themePreference: String) {
        when (themePreference) {
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AUTO_BATTERY_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            FOLLOW_SYSTEM_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
