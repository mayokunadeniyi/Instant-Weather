package com.mayokunadeniyi.instantweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherCondition
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherDescription
import com.mayokunadeniyi.instantweather.data.model.Wind

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */

@Entity(tableName = "weather_forecast")
class DBWeatherForecast(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,

    val wind: Wind,

    @ColumnInfo(name = "weather_description")
    val networkWeatherDescriptions: List<NetworkWeatherDescription>,

    @ColumnInfo(name = "weather_condition")
    val networkWeatherCondition: NetworkWeatherCondition
)