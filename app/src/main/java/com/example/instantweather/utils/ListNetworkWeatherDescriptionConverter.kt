package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.NetworkWeatherDescription
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class ListWeatherDtoConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<NetworkWeatherDescription?>?>() {}.type

    @TypeConverter
    fun fromWeatherDtoList(list: List<NetworkWeatherDescription?>?): String {
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toWeatherDtoList(json: String?): List<NetworkWeatherDescription> {
        return gson.fromJson(json, type)
    }

}