package com.mayokunadeniyi.instantweather.data.repository

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.*
import com.mayokunadeniyi.instantweather.data.remote.WeatherApi
import com.mayokunadeniyi.instantweather.mapper.WeatherMapperLocal
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.mapper.toDatabaseModel
import com.mayokunadeniyi.instantweather.utils.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */

class WeatherRepository(
    private val database: WeatherDatabase,
    application: Application
) : BaseViewModel(application) {

    private val weatherMapperLocal = WeatherMapperLocal()
    private var prefHelper = SharedPreferenceHelper.getInstance(application)
    private val weatherDao = database.weatherDao

    //This sets the time before data is gotten from remote to 15 minutes
    private var refreshTime = 900L * 1000L * 1000L * 1000L


    //Weather LiveData exposed to ViewModel
    private val _weather = MutableLiveData<Weather>()
    val weather = _weather.asLiveData()


    /**
     * This function helps to get [Weather] of a [location] either from the remote or local
     * depending on if the cache duration has expired.
     * @param location the location whose weather information is required
     */
    suspend fun initialWeatherFetch(location: Location): Result<Boolean> {
        checkWeatherCacheDuration(prefHelper.getUserSetCacheDuration())
        val initialWeatherFetch = prefHelper.getTimeOfInitialWeatherFetch()
        return if (initialWeatherFetch != null && initialWeatherFetch != 0L && (System.nanoTime() - initialWeatherFetch) < refreshTime) {
            getLocalWeatherData()
        } else {
            fetchRemoteWeatherData(location)
        }
    }


    /**
     * This function helps to get [Weather] of a [location] from the remote and saves it
     * into the database.
     * @param location the location whose weather information is required
     */
    suspend fun fetchRemoteWeatherData(location: Location): Result<Boolean> {
        Timber.i("Getting weather data from remote!")
        return try {
            val result = WeatherApi.retrofitService.getCurrentWeather(
                location.latitude, location.longitude, API_KEY
            )
            if (result.isSuccessful) {
                val networkWeather = result.body()
                prefHelper.saveCityId(networkWeather!!.cityId)
                storeRemoteWeatherDataLocally(networkWeather)
                Result.Success(true)
            } else {
                Result.Success(false)
            }
        } catch (error: IOException) {
            Result.Error(error)
        }
    }

    /**
     * This function is called after fresh [Weather] from the remote source has been
     * received as it stores the [networkWeather] into the database before updating the UI.
     * @see fetchRemoteWeatherData
     * @param networkWeather the [NetworkWeather] to be converted to a database model and stored.
     */
    private fun storeRemoteWeatherDataLocally(networkWeather: NetworkWeather) {
        launch {
            withContext(Dispatchers.IO) {
                weatherDao.deleteAllWeather()

                //Get the temperature in kelvin and convert to celsius
                val kelvinValue = networkWeather.networkWeatherCondition.temp
                networkWeather.networkWeatherCondition.temp = convertKelvinToCelsius(kelvinValue)
                val weatherResponse = networkWeather.toDatabaseModel()
                weatherDao.insertWeather(weatherResponse)

                val dbWeather = weatherDao.getWeather()
                _weather.postValue(weatherMapperLocal.transformToDomain(dbWeather))

            }
        }
        prefHelper.saveTimeOfInitialWeatherFetch(System.nanoTime())
    }


    /**
     * This function helps to get cached [Weather] information from the local db.
     */
    private fun getLocalWeatherData(): Result<Boolean> {
        Timber.i("Getting weather data from cache!")
        launch {
            withContext(Dispatchers.IO) {
                val dbWeather = weatherDao.getWeather()
                _weather.postValue(weatherMapperLocal.transformToDomain(dbWeather))
            }
        }
        return Result.Success(true)
    }


    /**
     * This function helps to check if the cache duration has expired or not
     */
    private fun checkWeatherCacheDuration(cachePreference: String?) {
        try {
            var cacheDuration = cachePreference?.toInt()
            if (cacheDuration == null || cacheDuration == 0) {
                cacheDuration = 900
            }
            refreshTime = cacheDuration.times(1000L * 1000L * 1000L)
        } catch (e: NumberFormatException) {
            Timber.e("Error $e")
        }

    }

}
