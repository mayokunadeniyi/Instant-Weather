package com.example.instantweather.utils

import androidx.room.TypeConverter
import com.example.instantweather.data.model.MainDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */
class MainDtoConverter {
    val gson = Gson()

    val type: Type = object : TypeToken<MainDto?>() {}.type

    @TypeConverter
    fun fromMainDto(mainDto: MainDto?): String {
        return gson.toJson(mainDto, type)
    }

    @TypeConverter
    fun toMainDto(json: String?): MainDto {
        return gson.fromJson(json, type)
    }
}