package com.mayokunadeniyi.instantweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeatherForecast

/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */

@Dao
interface WeatherForecastDao {
    //Saves the [WeatherForecast] into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastWeather(vararg dbWeatherForecast: DBWeatherForecast)

    //Returns a list of [DBWeatherForecast] ordered by their id
    @Query("SELECT * FROM weather_forecast ORDER BY id")
    suspend fun getAllWeatherForecast(): List<DBWeatherForecast>

    //Deletes all [WeatherForecast] in the table
    @Query("DELETE FROM weather_forecast")
    suspend fun deleteAllWeatherForecast()
}