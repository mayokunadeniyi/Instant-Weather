package com.mayokunadeniyi.instantweather.mapper

import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast

/**
 * Created by Mayokun Adeniyi on 15/03/2020.
 */

class WeatherForecastMapperLocal :
    BaseMapper<List<DBWeatherForecast>, List<WeatherForecast>> {
    override fun transformToDomain(type: List<DBWeatherForecast>): List<WeatherForecast> {
        return type.map { dbWeatherForecast ->
            WeatherForecast(
                dbWeatherForecast.id,
                dbWeatherForecast.date,
                dbWeatherForecast.wind,
                dbWeatherForecast.networkWeatherDescriptions,
                dbWeatherForecast.networkWeatherCondition
            )
        }
    }

    override fun transformToDto(type: List<WeatherForecast>): List<DBWeatherForecast> {
        return type.map { weatherForecast ->
            DBWeatherForecast(
                weatherForecast.uID,
                weatherForecast.date,
                weatherForecast.wind,
                weatherForecast.networkWeatherDescription,
                weatherForecast.networkWeatherCondition
            )
        }
    }

}

fun List<DBWeatherForecast>.toDomain() = WeatherForecastMapperLocal().transformToDomain(this)

fun WeatherForecast.toDbModel() = DBWeatherForecast(
    id = this.uID,
    date = this.date,
    wind = this.wind,
    networkWeatherDescriptions = this.networkWeatherDescription,
    networkWeatherCondition = this.networkWeatherCondition
)