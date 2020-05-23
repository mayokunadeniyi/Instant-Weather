package com.mayokunadeniyi.instantweather.data.local.datasource

import com.mayokunadeniyi.instantweather.data.local.dao.WeatherDao
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */

class WeatherLocalDataSourceImpl (private val weatherDao: WeatherDao,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): WeatherLocalDataSource{
    override suspend fun insertWeather(vararg dbWeather: DBWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun getWeather(): DBWeather {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWeather(): List<DBWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWeather() {
        TODO("Not yet implemented")
    }
}