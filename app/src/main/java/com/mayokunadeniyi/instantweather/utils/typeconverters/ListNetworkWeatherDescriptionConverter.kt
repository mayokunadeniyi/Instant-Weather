package com.mayokunadeniyi.instantweather.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherDescription
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class ListNetworkWeatherDescriptionConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<NetworkWeatherDescription?>?>() {}.type

    /**
     * Converts a listOf[NetworkWeatherDescription] to a [String]
     */
    @TypeConverter
    fun fromWeatherDtoList(list: List<NetworkWeatherDescription?>?): String {
        return gson.toJson(list, type)
    }

    /**
     * Converts a [String] to a listOf[NetworkWeatherDescription]
     */
    @TypeConverter
    fun toWeatherDtoList(json: String?): List<NetworkWeatherDescription> {
        return gson.fromJson(json, type)
    }
}
