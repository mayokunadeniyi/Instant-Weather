package com.example.instantweather.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */

//This class represents the Network DTO 
class NetworkWeather(

    val uId: Long,

    @SerializedName("id")
    val cityId: Long,

    val name: String,

    @SerializedName("weather")
    val networkWeatherDescriptions: List<NetworkWeatherDescription>,

    @SerializedName("main")
    val networkWeatherCondition: NetworkWeatherCondition
)


