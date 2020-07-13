package com.mayokunadeniyi.instantweather.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.remote.WeatherApi
import com.mayokunadeniyi.instantweather.mapper.WeatherForecastMapperLocal
import com.mayokunadeniyi.instantweather.mapper.toDatabaseModel
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

/**
 * Created by Mayokun Adeniyi on 05/06/2020.
 */

class ForecastRepository(
    database: WeatherDatabase,
    application: Application
) : BaseViewModel(application) {


    private val weatherForecastDao = database.weatherForecastDao
    private val weatherForecastMapperLocal = WeatherForecastMapperLocal()

    //WeatherForecast LiveData exposed to the ViewModel
    private val _weatherForecast = MutableLiveData<List<WeatherForecast>>()
    val weatherForecast = _weatherForecast.asLiveData()


    private var prefHelper = SharedPreferenceHelper.getInstance(application)

    //This sets the time before data is gotten from remote to 15 minutes
    private var refreshTime = 900L * 1000L * 1000L * 1000L


    /**
     * This function helps to get [WeatherForecast] of either from the remote or local
     * depending on if the cache duration has expired.
     */
    suspend fun initialForecastFetch(): Result<Boolean> {
        checkWeatherCacheDuration()
        val initialForecastFetchTime = prefHelper.getTimeOfInitialWeatherForecastFetch()
        return if (initialForecastFetchTime != null && initialForecastFetchTime != 0L && System.nanoTime() - initialForecastFetchTime < refreshTime) {
            getLocalWeatherForecastData()
        } else {
            fetchRemoteWeatherForecast()
        }
    }


    /**
     * This function helps to check if the cache duration has expired or not
     */
    private fun checkWeatherCacheDuration() {
        val cachePreference = prefHelper.getUserSetCacheDuration()
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


    /**
     * This function helps to get [WeatherForecast] from the remote and saves it
     * into the database. It uses the cityId of the location to get it's weather forecast.
     */
    suspend fun fetchRemoteWeatherForecast(): Result<Boolean> {
        Timber.i("Getting remote weather forecast....")
        val cityId = prefHelper.getCityId()
        if (cityId != null) {
            return try {
                val result = WeatherApi.retrofitService.getWeatherForecast(cityId, API_KEY)
                if (result.isSuccessful) {
                    val networkWeatherForecast = result.body()?.weathers
                    storeRemoteForecastDataLocally(networkWeatherForecast!!)
                    Result.Success(true)
                } else {
                    Result.Success(false)
                }
            } catch (error: IOException) {
                Result.Error(error)
            }
        } else {
            return Result.Success(false)
        }
    }

    /**
     * This function helps to get cached [WeatherForecast] information from the local db.
     */
    private fun getLocalWeatherForecastData(): Result<Boolean> {
        Timber.i("Getting local weather forecast....")
        launch {
            withContext(Dispatchers.IO) {
                //Get weather forecast from db
                val weatherForecastDb = weatherForecastDao.getAllWeatherForecast()
                _weatherForecast.postValue(
                    weatherForecastMapperLocal.transformToDomain(
                        weatherForecastDb
                    )
                )
            }
        }
        return Result.Success(true)
    }

    /**
     * This function is called after fresh [WeatherForecast] from the remote source has been
     * received as it stores the [listOfNetworkWeatherForecast] into the database before updating the UI.
     * @see fetchRemoteWeatherForecast
     * @param listOfNetworkWeatherForecast the list of [NetworkWeatherForecast] to be converted to a database model and stored.
     */
    private fun storeRemoteForecastDataLocally(listOfNetworkWeatherForecast: List<NetworkWeatherForecast>) {
        launch {
            withContext(Dispatchers.IO) {
                //Empty the db
                weatherForecastDao.deleteAllWeatherForecast()
                //Insert each weather forecast into the db
                for (weatherForecast in listOfNetworkWeatherForecast) {
                    //Get the temperature in kelvin and convert to celsius
                    val kelvinValue = weatherForecast.networkWeatherCondition.temp
                    weatherForecast.networkWeatherCondition.temp =
                        convertKelvinToCelsius(kelvinValue)

                    weatherForecastDao.insertForecastWeather(weatherForecast.toDatabaseModel())
                }

                val dbForecast = weatherForecastDao.getAllWeatherForecast()
                _weatherForecast.postValue(weatherForecastMapperLocal.transformToDomain(dbForecast))
            }
        }
        prefHelper.saveTimeOfInitialWeatherForecastFetch(System.nanoTime())
    }
}