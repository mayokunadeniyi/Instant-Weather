package com.mayokunadeniyi.instantweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mayokunadeniyi.instantweather.data.local.dao.WeatherDao
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.utils.typeconverters.CityConverter
import com.mayokunadeniyi.instantweather.utils.typeconverters.ListNetworkWeatherDescriptionConverter
import com.mayokunadeniyi.instantweather.utils.typeconverters.NetworkWeatherConditionConverter
import com.mayokunadeniyi.instantweather.utils.typeconverters.WindConverter

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Database(entities = [DBWeather::class,DBWeatherForecast::class],version = 1,exportSchema = false)
@TypeConverters(
    ListNetworkWeatherDescriptionConverter::class,
    NetworkWeatherConditionConverter::class,
    WindConverter::class,
    CityConverter::class)
abstract class WeatherDatabase : RoomDatabase(){

    abstract val weatherDao: WeatherDao

    companion object{
        @Volatile
        private var instance: WeatherDatabase? = null

        /**
         * This checks if there is an existing instance of the [WeatherDatabase] in the
         * specified [context] and creates one if there isn't or else, it returns the
         * already existing instance. This function ensures that the [WeatherDatabase] is
         * accessed at any instance by a single thread.
         */
        fun getInstance(context: Context): WeatherDatabase{
            synchronized(this) {
                var _instance = instance
                if (_instance == null) {
                    _instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    instance = _instance
                }
                return _instance
            }
        }
    }
}