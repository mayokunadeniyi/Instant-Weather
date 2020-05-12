package com.mayokunadeniyi.instantweather.utils.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mayokunadeniyi.instantweather.data.model.Sys
import com.mayokunadeniyi.instantweather.data.model.Wind
import java.lang.reflect.Type

class SysConverter {
    val gson = Gson()
    val type: Type = object : TypeToken<Sys?>() {}.type

    /**
     * Converts a [Sys] to a [String]
     */
    @TypeConverter
    fun fromSys(sys: Sys?): String {
        return gson.toJson(sys, type)
    }

    /**
     * Converts a [Sys] to a [String]
     */
    @TypeConverter
    fun toSys(string: String?): Sys {
        return gson.fromJson(string, type)
    }
}