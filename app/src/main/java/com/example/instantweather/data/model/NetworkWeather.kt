package com.example.instantweather.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */

//This class represents the Network DTO 
data class NetworkWeather(

    val uId: Int,

    @SerializedName("id")
    val cityId: Int,

    val name: String,

    val wind: Wind,

    @SerializedName("weather")
    val networkWeatherDescriptions: List<NetworkWeatherDescription>,

    @SerializedName("main")
    val networkWeatherCondition: NetworkWeatherCondition
)


