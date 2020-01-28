package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.CityWeatherDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class ListCityWeatherDtoConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<CityWeatherDto?>?>() {}.type

    @TypeConverter
    fun fromCityWeatherDtoList(list: List<CityWeatherDto?>?): String {
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toCityWeatherDtoList(json: String?): List<CityWeatherDto> {
        return gson.fromJson(json, type)
    }
}