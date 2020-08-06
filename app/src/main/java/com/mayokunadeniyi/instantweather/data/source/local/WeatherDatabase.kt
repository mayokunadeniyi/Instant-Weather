package com.mayokunadeniyi.instantweather.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mayokunadeniyi.instantweather.data.source.local.dao.WeatherDao
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.utils.typeconverters.ListNetworkWeatherDescriptionConverter

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Database(entities = [DBWeather::class, DBWeatherForecast::class], version = 1, exportSchema = true)
@TypeConverters(
    ListNetworkWeatherDescriptionConverter::class
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao
}
