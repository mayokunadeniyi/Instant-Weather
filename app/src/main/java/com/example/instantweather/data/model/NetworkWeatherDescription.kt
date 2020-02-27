package com.example.instantweather.data.model

data class NetworkWeatherDescription(
    val id: Long,
    val main: String?,
    val description: String?,
    val icon: String?
)