package com.mayokunadeniyi.instantweather.data.model

import com.google.gson.annotations.SerializedName


//This class represents the DTO for the weather forecast
data class NetworkWeatherForecast(

    val id: Int,

    @SerializedName("dt_txt")
    val date: String,

    val wind: Wind,

    @SerializedName("weather")
    val networkWeatherDescription: List<NetworkWeatherDescription>,

    @SerializedName("main")
    val networkWeatherCondition: NetworkWeatherCondition
)