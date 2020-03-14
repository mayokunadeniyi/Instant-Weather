package com.example.instantweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.instantweather.data.model.NetworkWeatherForecast

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */

@Entity(tableName = "weather_forecast")
class WeatherForecast(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "weathers")
    val weathers: NetworkWeatherForecast
)