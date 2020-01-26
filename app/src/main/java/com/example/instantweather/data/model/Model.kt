package com.example.instantweather.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */

class CityWeather(
    val id: Long,
    val name: String,

    @SerializedName("weather")
    val weathers: List<Weather>,

    val main: MainOb
)


class MainOb(
    val temp: Double,
    val pressure: Double,
    val humidity: Double
)

class Weather(
    val id: Long,
    val main: String?,
    val description: String?,
    val icon: String?
)