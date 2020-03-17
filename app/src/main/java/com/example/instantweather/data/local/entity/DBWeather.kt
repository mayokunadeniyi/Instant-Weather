package com.example.instantweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.instantweather.data.model.NetworkWeatherCondition
import com.example.instantweather.data.model.NetworkWeatherDescription
import com.example.instantweather.data.model.Wind

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

//This class represents the Database DTO
@Entity(tableName = "weather_table")
data class DBWeather(

    @ColumnInfo(name = "unique_id")
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0,

    @ColumnInfo(name = "city_id")
    val cityId: Int,

    @ColumnInfo(name = "city_name")
    val cityName: String,

    val wind: Wind,

    @ColumnInfo(name = "weather_details")
    val networkWeatherDescription: List<NetworkWeatherDescription>,

    @ColumnInfo(name = "main_details")
    val networkWeatherCondition: NetworkWeatherCondition
)