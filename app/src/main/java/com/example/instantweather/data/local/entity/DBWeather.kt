package com.example.instantweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.instantweather.data.model.NetworkWeatherCondition
import com.example.instantweather.data.model.NetworkWeatherDescription

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

//This class represents the Database DTO
@Entity(tableName = "weather_table")
class DBWeather(

    @ColumnInfo(name = "city_id")
    val cityId: Long,

    @ColumnInfo(name = "city_name")
    val cityName: String?,

    @ColumnInfo(name = "weather_details")
    val networkWeatherDescription: List<NetworkWeatherDescription>,

    @ColumnInfo(name = "main_details")
    val networkWeatherCondition: NetworkWeatherCondition
){
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0
}