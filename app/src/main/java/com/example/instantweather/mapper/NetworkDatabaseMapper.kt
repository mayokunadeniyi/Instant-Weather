package com.example.instantweather.mapper

import com.example.instantweather.data.local.entity.DBWeather
import com.example.instantweather.data.local.entity.DBWeatherForecast
import com.example.instantweather.data.model.NetworkWeather
import com.example.instantweather.data.model.NetworkWeatherForecast

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */


fun NetworkWeather.toDatabaseModel() = DBWeather(
    uId = this.uId,
    cityId = this.cityId,
    cityName = this.name,
    wind = this.wind,
    networkWeatherDescription = this.networkWeatherDescriptions,
    networkWeatherCondition = this.networkWeatherCondition
)

fun NetworkWeatherForecast.toDatabaseModel() = DBWeatherForecast(
   id = this.id,
    date = this.date,
    wind = this.wind,
    networkWeatherDescriptions = this.networkWeatherDescription,
    networkWeatherCondition = this.networkWeatherCondition
)