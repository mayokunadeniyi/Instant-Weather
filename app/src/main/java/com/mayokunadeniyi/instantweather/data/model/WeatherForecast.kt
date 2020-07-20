package com.mayokunadeniyi.instantweather.data.model

import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast

/**
 * Created by Mayokun Adeniyi on 11/03/2020.
 */

data class WeatherForecast(
    val uID: Int,

    val date: String,

    val wind: Wind,

    val networkWeatherDescription: List<NetworkWeatherDescription>,

    val networkWeatherCondition: NetworkWeatherCondition
)