package com.example.instantweather

import android.app.Application
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.instantweather.utils.ThemeManager
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class InstantWeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initTheme()
    }

    private fun initTheme(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        ThemeManager.applyTheme(preferences.getString("theme_key","")!!)
    }
}