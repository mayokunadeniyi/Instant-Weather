package com.example.instantweather.data.local.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.instantweather.data.local.entity.DBWeather

/**
 * Created by Mayokun Adeniyi on 2020-01-27.
 */

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg dbWeather: DBWeather)

    @Query("SELECT * FROM weather_table ORDER BY unique_id DESC LIMIT 1")
    fun getWeather(): LiveData<DBWeather>

    @Query("SELECT * FROM weather_table ORDER BY unique_id DESC")
    fun getAllWeather(): LiveData<List<DBWeather>>

    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeather()
}