package com.mayokunadeniyi.instantweather.data.local.datasource

import com.mayokunadeniyi.instantweather.data.local.entity.DBWeatherForecast

/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */
interface WeatherForecastLocalDataSource {

    suspend fun insertForecastWeather(vararg dbWeatherForecast: DBWeatherForecast)
    suspend fun getAllWeatherForecast(): List<DBWeatherForecast>
    suspend fun deleteAllWeatherForecast()
}