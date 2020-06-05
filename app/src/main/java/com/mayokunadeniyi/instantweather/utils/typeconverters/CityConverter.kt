package com.mayokunadeniyi.instantweather.utils.typeconverters

import androidx.room.TypeConverter
import com.mayokunadeniyi.instantweather.data.model.City
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 16/03/2020.
 */

class CityConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<City?>() {}.type


    /**
     * Converts a [City] to a [String]
     */
    @TypeConverter
    fun fromCity(city: City?): String{
        return gson.toJson(city,type)
    }

    /**
     * Converts a [String] to a [City]
     */
    @TypeConverter
    fun toCity(json: String?): City {
        return gson.fromJson(json,type)
    }
}