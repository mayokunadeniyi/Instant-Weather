package com.mayokunadeniyi.instantweather.mapper

import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast

/**
 * Created by Mayokun Adeniyi on 15/03/2020.
 */

class WeatherForecastMapperRemote :
    BaseMapper<List<NetworkWeatherForecast>, List<WeatherForecast>> {
    override fun transformToDomain(type: List<NetworkWeatherForecast>): List<WeatherForecast> {
        return type.map { networkWeatherForecast ->
            WeatherForecast(
                networkWeatherForecast.id,
                networkWeatherForecast.date,
                networkWeatherForecast.wind,
                networkWeatherForecast.networkWeatherDescription,
                networkWeatherForecast.networkWeatherCondition
            )
        }
    }

    override fun transformToDto(type: List<WeatherForecast>): List<NetworkWeatherForecast> {
        return type.map { weatherForecast ->
            NetworkWeatherForecast(
                weatherForecast.uID,
                weatherForecast.date,
                weatherForecast.wind,
                weatherForecast.networkWeatherDescription,
                weatherForecast.networkWeatherCondition
            )
        }
    }
}
