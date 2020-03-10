package com.example.instantweather.mapper

import com.example.instantweather.data.local.entity.DBWeather
import com.example.instantweather.data.model.Weather

/**
 * Created by Mayokun Adeniyi on 10/03/2020.
 */

class WeatherMapperLocal : BaseMapperRepository<DBWeather,Weather>{
    override fun transform(type: DBWeather): Weather = Weather(
        uId = type.uId,
        cityId = type.cityId,
        name = type.cityName,
        networkWeatherDescription = type.networkWeatherDescription,
        networkWeatherCondition = type.networkWeatherCondition
    )

    override fun transformToRepository(type: Weather): DBWeather = DBWeather(
        uId = type.uId,
        cityId = type.cityId,
        cityName = type.name,
        networkWeatherDescription = type.networkWeatherDescription,
        networkWeatherCondition = type.networkWeatherCondition
    )

}