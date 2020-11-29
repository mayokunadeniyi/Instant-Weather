package com.mayokunadeniyi.instantweather

import android.app.Application
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.mayokunadeniyi.instantweather.di.AppComponent
import com.mayokunadeniyi.instantweather.di.DaggerAppComponent
import com.mayokunadeniyi.instantweather.di.module.AppModule
import com.mayokunadeniyi.instantweather.utils.ThemeManager
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
    lateinit var injector: DispatchingAndroidInjector<Any>

    val appComponent: AppComponent?
        get() = component

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()

        component.inject(this)

        Timber.plant(Timber.DebugTree())
        initTheme()
    }

    /**
     * Commented this out since we already inject weather repository.
     *
     * val weatherRepository: WeatherRepository
     * get() = ServiceLocator.provideWeatherRepository(this)
     */

    private fun initTheme() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        ThemeManager.applyTheme(preferences.getString("theme_key", "")!!)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()
        // commented addFactory worker for MyWorkerFactory since
        // it requires an instance of repository and we already
        // inject it with repository instance.
        // but you can disable this and work do around.

        // myWorkerFactory.addFactory(MyWorkerFactory())
        // Add here other factories that you may need in this application

        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(myWorkerFactory)
            .build()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return injector
    }
}
