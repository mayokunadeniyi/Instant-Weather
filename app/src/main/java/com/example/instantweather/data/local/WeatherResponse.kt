package com.example.instantweather.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.instantweather.data.model.CityWeatherDto
import com.example.instantweather.data.model.MainDto

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Entity
class WeatherResponse(

    @ColumnInfo(name = "city_id")
    val cityId: Long,

    @ColumnInfo(name = "city_name")
    val cityName: String?,

    @ColumnInfo(name = "weather_details")
    val weatherDto: CityWeatherDto,

    @ColumnInfo(name = "main_details")
    val mainDto: MainDto
){
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0
}