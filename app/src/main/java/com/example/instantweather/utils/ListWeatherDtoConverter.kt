package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.WeatherDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class ListWeatherDtoConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<WeatherDto?>?>() {}.type

    @TypeConverter
    fun fromWeatherDtoList(list: List<WeatherDto?>?): String {
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toWeatherDtoList(json: String?): List<WeatherDto> {
        return gson.fromJson(json, type)
    }

}