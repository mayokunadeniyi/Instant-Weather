package com.mayokunadeniyi.instantweather.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherCondition
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class NetworkWeatherConditionConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<NetworkWeatherCondition?>() {}.type

    /**
     * Converts a [NetworkWeatherCondition] to a [String]
     */
    @TypeConverter
    fun fromMainDto(networkWeatherCondition: NetworkWeatherCondition?): String {
        return gson.toJson(networkWeatherCondition, type)
    }

    /**
     * Converts a [String] to a [NetworkWeatherCondition]
     */
    @TypeConverter
    fun toMainDto(json: String?): NetworkWeatherCondition {
        return gson.fromJson(json, type)
    }
}
