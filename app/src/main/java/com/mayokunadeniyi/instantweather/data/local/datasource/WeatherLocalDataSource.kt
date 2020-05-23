package com.mayokunadeniyi.instantweather.data.local.datasource

import com.mayokunadeniyi.instantweather.data.local.entity.DBWeather

/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */

interface WeatherLocalDataSource {
    suspend fun insertWeather(vararg dbWeather: DBWeather)
    suspend fun getWeather(): DBWeather
    suspend fun getAllWeather(): List<DBWeather>
    suspend fun deleteAllWeather()
}