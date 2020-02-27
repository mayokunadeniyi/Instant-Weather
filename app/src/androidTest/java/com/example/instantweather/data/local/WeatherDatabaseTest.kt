package com.example.instantweather.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.instantweather.data.local.dao.WeatherDao
import com.example.instantweather.data.local.entity.DBWeather
import com.example.instantweather.data.model.NetworkWeatherCondition
import com.example.instantweather.data.model.NetworkWeatherDescription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Mayokun Adeniyi on 26/02/2020.
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WeatherDatabaseTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var weatherDb: WeatherDatabase

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        weatherDb = Room.inMemoryDatabaseBuilder(
           context,WeatherDatabase::class.java
        ).build()

        weatherDao = weatherDb.weatherDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        weatherDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertWeatherAndReadInList(){
        val weatherDto = NetworkWeatherDescription(1L,"MockStuff","MockStuff","MockStuff")
        val mainDto = NetworkWeatherCondition(20.0,30.0,40.0)
        val weather = DBWeather(1L,"Lagos", listOf(weatherDto),mainDto)

        runBlocking {
            weatherDao.insertWeather(weather)
            val weatherReturnList = weatherDao.getAllWeather()
            assertThat(weatherReturnList.size,equalTo(1))

        }

    }

    @Test
    @Throws(Exception::class)
    fun insertWeatherAndReadData(){
        val weatherDto = NetworkWeatherDescription(1L,"MockStuff","MockStuff","MockStuff")
        val mainDto = NetworkWeatherCondition(20.0,30.0,40.0)
        val weather = DBWeather(1L,"Lagos", listOf(weatherDto),mainDto)

        runBlocking {
            weatherDao.insertWeather(weather)
            val weatherReturnList = weatherDao.getAllWeather()
            val returnedWeather = weatherReturnList[0]
            assertThat(returnedWeather.cityName,equalTo("Lagos"))
        }

    }


}