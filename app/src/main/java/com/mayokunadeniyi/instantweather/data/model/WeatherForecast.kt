package com.mayokunadeniyi.instantweather.data.model

/**
 * Created by Mayokun Adeniyi on 11/03/2020.
 */

data class WeatherForecast(
    val uID: Int,

    var date: String,

    val wind: Wind,

    val networkWeatherDescription: List<NetworkWeatherDescription>,

    val networkWeatherCondition: NetworkWeatherCondition
)
