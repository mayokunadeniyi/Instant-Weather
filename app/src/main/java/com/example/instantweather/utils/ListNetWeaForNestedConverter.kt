package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.NetworkWeatherForecastResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 14/03/2020.
 */

class ListNetWeaForNestedConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<NetworkWeatherForecastResponse?>?>() {}.type

    @TypeConverter
    fun fromNetWeaForNestList(list: List<NetworkWeatherForecastResponse?>?): String{
        return gson.toJson(list,type)
    }

    @TypeConverter
    fun toNetWeaForNestList(json: String?): List<NetworkWeatherForecastResponse>{
        return gson.fromJson(json,type)
    }
}