package com.example.instantweather.data.model

/**
 * Created by Mayokun Adeniyi on 11/03/2020.
 */

//Domain model for weather forecast
data class WeatherForecast(
    val uID: Int,

    val date: String,

    val wind: Wind,

    val networkWeatherDescription: List<NetworkWeatherDescription>,

    val networkWeatherCondition: NetworkWeatherCondition
)