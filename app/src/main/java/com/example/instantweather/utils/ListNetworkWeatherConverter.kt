package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.NetworkWeather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class ListCityWeatherDtoConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<NetworkWeather?>?>() {}.type

    @TypeConverter
    fun fromCityWeatherDtoList(list: List<NetworkWeather?>?): String {
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toCityWeatherDtoList(json: String?): List<NetworkWeather> {
        return gson.fromJson(json, type)
    }
}