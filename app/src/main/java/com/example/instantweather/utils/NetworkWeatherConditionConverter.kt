package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.NetworkWeatherCondition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class MainDtoConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<NetworkWeatherCondition?>() {}.type

    @TypeConverter
    fun fromMainDto(networkWeatherCondition: NetworkWeatherCondition?): String {
        return gson.toJson(networkWeatherCondition, type)
    }

    @TypeConverter
    fun toMainDto(json: String?): NetworkWeatherCondition {
        return gson.fromJson(json, type)
    }
}