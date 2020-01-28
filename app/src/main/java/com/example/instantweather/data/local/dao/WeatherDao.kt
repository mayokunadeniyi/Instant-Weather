package com.example.instantweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.instantweather.data.local.entity.WeatherResponse

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg weatherResponse: WeatherResponse)

    @Query("SELECT * FROM weather_response ORDER BY uId DESC LIMIT 1")
    suspend fun getWeather(): WeatherResponse

    @Query("SELECT * FROM weather_response ORDER BY uId DESC")
    suspend fun getAllWeather(): List<WeatherResponse>

    @Query("DELETE FROM weather_response")
    suspend fun deleteAllWeather()
}