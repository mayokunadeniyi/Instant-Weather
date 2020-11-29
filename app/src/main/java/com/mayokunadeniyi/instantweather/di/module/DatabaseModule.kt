package com.mayokunadeniyi.instantweather.di.module

import android.app.Application
import androidx.room.Room
import com.mayokunadeniyi.instantweather.data.source.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.source.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Force Porquillo on 29/011/2020.
 */

@Suppress("unused")
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(
        application: Application
    ): WeatherDatabase = Room
        .databaseBuilder(
            application.applicationContext,
            WeatherDatabase::class.java,
            "InstantWeather.db"
        ).build()


    @Singleton
    @Provides
    fun providesWeatherDao(
        database: WeatherDatabase
    ): WeatherDao = database.weatherDao
}