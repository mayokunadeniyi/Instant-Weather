package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.NetworkWeatherForecastNested
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 14/03/2020.
 */

class ListNetWeaForNestedConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<List<NetworkWeatherForecastNested?>?>() {}.type

    @TypeConverter
    fun fromNetWeaForNestList(list: List<NetworkWeatherForecastNested?>?): String{
        return gson.toJson(list,type)
    }

    @TypeConverter
    fun toNetWeaForNestList(json: String?): List<NetworkWeatherForecastNested>{
        return gson.fromJson(json,type)
    }
}