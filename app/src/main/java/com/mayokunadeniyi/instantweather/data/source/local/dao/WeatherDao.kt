package com.mayokunadeniyi.instantweather.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg dbWeather: DBWeather)

    @Query("SELECT * FROM weather_table ORDER BY unique_id DESC LIMIT 1")
    suspend fun getWeather(): DBWeather

    @Query("SELECT * FROM weather_table ORDER BY unique_id DESC")
    suspend fun getAllWeather(): List<DBWeather>

    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeather()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastWeather(vararg dbWeatherForecast: DBWeatherForecast)

    @Query("SELECT * FROM weather_forecast ORDER BY id")
    suspend fun getAllWeatherForecast(): List<DBWeatherForecast>

    @Query("DELETE FROM weather_forecast")
    suspend fun deleteAllWeatherForecast()
}
