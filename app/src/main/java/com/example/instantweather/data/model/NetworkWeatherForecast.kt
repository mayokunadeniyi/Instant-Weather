package com.example.instantweather.data.model

import com.google.gson.annotations.SerializedName


data class NetworkWeatherForecast(
    @SerializedName("list")
    val weathers: List<NetworkWeather>
)