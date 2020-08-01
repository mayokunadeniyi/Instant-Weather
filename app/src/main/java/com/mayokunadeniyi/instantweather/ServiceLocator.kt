package com.mayokunadeniyi.instantweather

import android.content.Context
import androidx.room.Room
import com.mayokunadeniyi.instantweather.data.source.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSource
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSourceImpl
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSourceImpl
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepositoryImpl

/**
 * Created by Mayokun Adeniyi on 20/07/2020.
 */

object ServiceLocator {

    private val lock = Any()
    private var database: WeatherDatabase? = null

    @Volatile
    var weatherRepository: WeatherRepository? = null

    fun provideWeatherRepository(context: Context): WeatherRepository {
        synchronized(this) {
            return weatherRepository ?: createWeatherRepository(context)
        }
    }

    private fun createWeatherRepository(context: Context): WeatherRepository {
        val newRepo = WeatherRepositoryImpl(
            WeatherRemoteDataSourceImpl(),
            createLocalWeatherSource(context)
        )
        weatherRepository = newRepo
        return newRepo
    }

    private fun createLocalWeatherSource(context: Context): WeatherLocalDataSource {
        val database = database ?: createDatabase(context)
        return WeatherLocalDataSourceImpl(database.weatherDao)
    }

    private fun createDatabase(context: Context): WeatherDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "InstantWeather.db"
        ).build()
        database = result
        return result
    }
}
