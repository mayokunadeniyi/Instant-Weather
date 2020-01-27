package com.example.instantweather.data.model

import com.example.instantweather.data.local.entity.WeatherResponse
import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */

class CityWeatherDto(

    val uId: Long,

    @SerializedName("id")
    val cityId: Long,

    val name: String,

    @SerializedName("weather")
    val weatherDtos: List<WeatherDto>,

    val main: MainDto
)


class MainDto(
    val temp: Double,
    val pressure: Double,
    val humidity: Double
)

class WeatherDto(
    val id: Long,
    val main: String?,
    val description: String?,
    val icon: String?
)


fun CityWeatherDto.toDatabaseModel(): WeatherResponse{
    return WeatherResponse(
        cityId = this.cityId,
        cityName = this.name,
        weatherDto = this.weatherDtos,
        mainDto = this.main
    )
}