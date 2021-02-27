package com.mayokunadeniyi.instantweather

import android.app.Application
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.di.AppInjector
import com.mayokunadeniyi.instantweather.utils.ThemeManager
import com.mayokunadeniyi.instantweather.worker.MyWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class InstantWeatherApplication : Application(), Configuration.Provider, HasAndroidInjector {

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
        initTheme()
    }

    private fun initTheme() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        runCatching {
            ThemeManager.applyTheme(requireNotNull(preferences.getString("theme_key", "")))
        }.onFailure { exception ->
            Timber.e("Theme Manager: $exception")
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()
        myWorkerFactory.addFactory(MyWorkerFactory(weatherRepository))
        // Add here other factories that you may need in this application

        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(myWorkerFactory)
            .build()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}
