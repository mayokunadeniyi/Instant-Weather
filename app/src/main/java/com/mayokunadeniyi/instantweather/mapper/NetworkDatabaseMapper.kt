package com.mayokunadeniyi.instantweather.mapper

import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast

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