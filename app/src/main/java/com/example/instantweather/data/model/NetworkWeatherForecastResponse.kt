package com.example.instantweather.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 13/03/2020.
 */

data class NetworkWeatherForecastNested(

    @SerializedName("weather")
    val networkWeatherDescriptions: List<NetworkWeatherDescription>,

    @SerializedName("main")
    val networkWeatherCondition: NetworkWeatherCondition,

    @SerializedName("dt_txt")
    val date: String,

    val wind: Double
)