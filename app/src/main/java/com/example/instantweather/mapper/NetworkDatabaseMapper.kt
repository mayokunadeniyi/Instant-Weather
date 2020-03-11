package com.example.instantweather.utils

import com.example.instantweather.data.local.entity.DBWeather
import com.example.instantweather.data.model.NetworkWeather

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */


fun NetworkWeather.toDatabaseModel() = DBWeather(
    cityId = this.cityId,
    cityName = this.name,
    networkWeatherDescription = this.networkWeatherDescriptions,
    networkWeatherCondition = this.networkWeatherCondition
)