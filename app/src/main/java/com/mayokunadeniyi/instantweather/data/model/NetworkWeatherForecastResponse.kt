package com.mayokunadeniyi.instantweather.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mayokun Adeniyi on 13/03/2020.
 */

data class NetworkWeatherForecastResponse(

    @SerializedName("list")
    val weathers: List<NetworkWeatherForecast>,

    val city: City
)
