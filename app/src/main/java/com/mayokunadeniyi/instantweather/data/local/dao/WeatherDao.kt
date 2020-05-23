package com.mayokunadeniyi.instantweather.data.local.dao

import androidx.room.*
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.local.entity.DBWeatherForecast

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Dao
interface WeatherDao {

    //Saves the [Weather] into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg dbWeather: DBWeather)

    //Returns a single [Weather] ordered by their id in descending order
    @Query("SELECT * FROM weather_table ORDER BY unique_id DESC LIMIT 1")
    suspend fun getWeather(): DBWeather

    //Returns a list of [DBWeather] ordered by their id in descending order
    @Query("SELECT * FROM weather_table ORDER BY unique_id DESC")
    suspend fun getAllWeather():List<DBWeather>

    //Deletes all [Weather] in the table
    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeather()
}