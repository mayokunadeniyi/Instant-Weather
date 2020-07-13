package com.mayokunadeniyi.instantweather

import android.app.Application
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.mayokunadeniyi.instantweather.di.dbModule
import com.mayokunadeniyi.instantweather.di.repositoryModule
import com.mayokunadeniyi.instantweather.di.viewModelModule
import com.mayokunadeniyi.instantweather.utils.ThemeManager
import com.mayokunadeniyi.instantweather.worker.MyWorkerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class InstantWeatherApplication: Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initTheme()

        startKoin {
            androidContext(this@InstantWeatherApplication)
            modules(listOf(dbModule, repositoryModule, viewModelModule))
        }
    }

    private fun initTheme(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        ThemeManager.applyTheme(preferences.getString("theme_key","")!!)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()
        myWorkerFactory.addFactory(MyWorkerFactory(this))
        // Add here other factories that you may need in this application

        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(myWorkerFactory)
            .build()
    }
}