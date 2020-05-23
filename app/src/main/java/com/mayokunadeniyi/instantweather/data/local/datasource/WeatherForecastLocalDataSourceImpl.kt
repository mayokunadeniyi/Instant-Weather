package com.mayokunadeniyi.instantweather.data.local.datasource

import com.mayokunadeniyi.instantweather.data.local.dao.WeatherForecastDao
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeatherForecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */
class WeatherForecastLocalDataSourceImpl(private val weatherForecastDao: WeatherForecastDao,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): WeatherForecastLocalDataSource {
    override suspend fun insertForecastWeather(vararg dbWeatherForecast: DBWeatherForecast) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWeatherForecast(): List<DBWeatherForecast> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWeatherForecast() {
        TODO("Not yet implemented")
    }
}