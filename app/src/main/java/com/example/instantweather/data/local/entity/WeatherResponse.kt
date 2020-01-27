package com.example.instantweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.instantweather.data.model.MainDto
import com.example.instantweather.data.model.WeatherDto

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Entity(tableName = "weather_response")
class WeatherResponse(

    @ColumnInfo(name = "city_id")
    val cityId: Long,

    @ColumnInfo(name = "city_name")
    val cityName: String?,

    @ColumnInfo(name = "weather_details")
    val weatherDto: List<WeatherDto>,

    @ColumnInfo(name = "main_details")
    val mainDto: MainDto
){
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0
}