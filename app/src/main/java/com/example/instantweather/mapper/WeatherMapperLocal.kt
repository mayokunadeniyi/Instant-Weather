package com.example.instantweather.mapper

import com.example.instantweather.data.local.entity.DBWeather
import com.example.instantweather.data.model.Weather

/**
 * Created by Mayokun Adeniyi on 10/03/2020.
 */

class WeatherMapperLocal : BaseMapper<DBWeather,Weather>{
    override fun transformToDomain(type: DBWeather): Weather = Weather(
        uId = type.uId,
        cityId = type.cityId,
        name = type.cityName,
        wind = type.wind,
        networkWeatherDescription = type.networkWeatherDescription,
        networkWeatherCondition = type.networkWeatherCondition
    )

    override fun transformToDto(type: Weather): DBWeather = DBWeather(
        uId = type.uId,
        cityId = type.cityId,
        cityName = type.name,
        wind = type.wind,
        networkWeatherDescription = type.networkWeatherDescription,
        networkWeatherCondition = type.networkWeatherCondition
    )

}