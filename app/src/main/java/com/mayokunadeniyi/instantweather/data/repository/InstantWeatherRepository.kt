package com.mayokunadeniyi.instantweather.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.BuildConfig
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.*
import com.mayokunadeniyi.instantweather.data.remote.WeatherApi
import com.mayokunadeniyi.instantweather.mapper.WeatherForecastMapperLocal
import com.mayokunadeniyi.instantweather.mapper.WeatherMapperLocal
import com.mayokunadeniyi.instantweather.mapper.WeatherMapperRemote
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.mayokunadeniyi.instantweather.mapper.toDatabaseModel
import com.mayokunadeniyi.instantweather.utils.convertKelvinToCelsius
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */

class InstantWeatherRepository(
    private val database: WeatherDatabase,
    application: Application
) : BaseViewModel(application) {

    private val weatherMapperLocal = WeatherMapperLocal()
    private val weatherForecastMapperLocal = WeatherForecastMapperLocal()
    private val weatherMapperRemote = WeatherMapperRemote()
    private var prefHelper = SharedPreferenceHelper.getInstance(application)

    //This sets the time before data is gotten from remote to 15 minutes
    private var refreshTime = 900L * 1000L * 1000L * 1000L
    private val API_KEY = BuildConfig.API_KEY
    private val dao = database.weatherDao

    //Weather[Domain Model] exposed to be used in the HomeFragmentViewModel
    val weather = MutableLiveData<Weather>()
    val weatherDataFetchState = MutableLiveData<Boolean>()
    val weatherIsLoading = MutableLiveData<Boolean>()

    //WeatherForecast[Domain Model] exposed to be used in the ForecastFragmentViewModel
    val weatherForecast = MutableLiveData<List<WeatherForecast>>()
    val weatherForecastDataFetchState = MutableLiveData<Boolean>()
    val weatherForecastIsLoading = MutableLiveData<Boolean>()

    //Weather[Domain Model] exposed to be used in the SearchFragmentViewModel
    val searchWeather = MutableLiveData<Weather>()
    val searchWeatherState = MutableLiveData<Boolean>()
    val searchWeatherIsLoading = MutableLiveData<Boolean>()

    /**
     * This function helps to get [Weather] of a [location] either from the remote or local
     * depending on if the cache duration has expired.
     * @param location the location whose weather information is required
     */
    fun refreshWeatherData(location: LocationModel?) {
        weatherIsLoading.value = true
        checkWeatherCacheDuration()
        val initialWeatherFetch = prefHelper.getTimeOfInitialWeatherFetch()
        if (initialWeatherFetch != null && initialWeatherFetch != 0L && (System.nanoTime() - initialWeatherFetch) < refreshTime) {
            getLocalWeatherData()
        } else {
            getRemoteWeatherData(location)
        }
    }

    /**
     * This function helps to get [WeatherForecast] of either from the remote or local
     * depending on if the cache duration has expired.
     */
    fun refreshWeatherForecastData() {
        weatherForecastIsLoading.value = true
        checkWeatherCacheDuration()
        val initialForecastFetchTime = prefHelper.getTimeOfInitialWeatherForecastFetch()
        if (initialForecastFetchTime != null && initialForecastFetchTime != 0L && System.nanoTime() - initialForecastFetchTime < refreshTime) {
            getLocalWeatherForecastData()
        } else {
            getRemoteWeatherForecast()
        }
    }

    /**
     * This function helps to check if the cache duration has expired or not
     */
    private fun checkWeatherCacheDuration() {
        val cachePreference = prefHelper.getUserSetCacheDuration()
        try {
            var cacheDuration = cachePreference?.toInt()
            if (cacheDuration == null || cacheDuration == 0){
                cacheDuration = 900
            }
            refreshTime = cacheDuration.times(1000L * 1000L * 1000L)
        } catch (e: NumberFormatException) {
            Timber.e("Error $e")
        }

    }

    /**
     * This function helps to get [Weather] of a [location] from the remote and saves it
     * into the database.
     * @param location the location whose weather information is required
     */
    fun getRemoteWeatherData(location: LocationModel?) {
        Timber.i("Getting weather data from remote!")
        if (location != null) {
            launch {
                try {
                    val networkWeather =
                        WeatherApi.retrofitService.getCurrentWeather(
                            location.latitude,
                            location.longitude,
                            API_KEY
                        )
                    //Save city ID to shared preferences
                    prefHelper.saveCityId(networkWeather.cityId)
                    storeRemoteWeatherDataLocally(networkWeather)
                } catch (e: Exception) {
                    weatherIsLoading.postValue(false)
                    weatherDataFetchState.postValue(false)
                    Timber.i("An error occurred ${e.message}")
                }
            }
        }
    }

    /**
     * This function helps to get cached [Weather] information from the local db.
     */
    private fun getLocalWeatherData() {
        Timber.i("Getting weather data from cache!")
        launch {
            withContext(Dispatchers.IO) {
                //Get the weather from the database
                val dbWeather = dao.getWeather()

                //Set the value for the MutableLiveData of type Weather
                weather.postValue(weatherMapperLocal.transformToDomain(dbWeather))
                weatherIsLoading.postValue(false)
                weatherDataFetchState.postValue(true)
            }
        }
    }

    /**
     * This function is called after fresh [Weather] from the remote source has been
     * received as it stores the [networkWeather] into the database before updating the UI.
     * @see getRemoteWeatherData
     * @param networkWeather the [NetworkWeather] to be converted to a database model and stored.
     */
    private fun storeRemoteWeatherDataLocally(networkWeather: NetworkWeather) {
        launch {
            withContext(Dispatchers.IO) {
                //Empty the db
                dao.deleteAllWeather()

                //Get the temperature in kelvin and convert to celsius
                val kelvinValue = networkWeather.networkWeatherCondition.temp
                networkWeather.networkWeatherCondition.temp = convertKelvinToCelsius(kelvinValue)

                //Convert the network weather response to a database model
                val weatherResponse = networkWeather.toDatabaseModel()

                //Insert the weather of database model into the db
                dao.insertWeather(weatherResponse)

                //Get the weather from the database
                val dbWeather = dao.getWeather()

                //Set the value for the MutableLiveData of type Weather
                weather.postValue(weatherMapperLocal.transformToDomain(dbWeather))
                weatherIsLoading.postValue(false)
                weatherDataFetchState.postValue(true)
            }
        }
        prefHelper.saveTimeOfInitialWeatherFetch(System.nanoTime())
    }

    /**
     * This function helps to get [WeatherForecast] from the remote and saves it
     * into the database. It uses the cityId of the location to get it's weather forecast.
     */
    fun getRemoteWeatherForecast() {
        launch {
            Timber.i("Getting remote weather forecast....")
            val cityId = prefHelper.getCityId()
            if (cityId != null)
                try {
                    val networkWeatherForecastResponse =
                        WeatherApi.retrofitService.getWeatherForecast(cityId, API_KEY)
                    val listOfNetworkWeatherForecast = networkWeatherForecastResponse.weathers
                    storeRemoteForecastDataLocally(listOfNetworkWeatherForecast)
                } catch (e: Exception) {
                    weatherForecastIsLoading.postValue(false)
                    weatherForecastDataFetchState.postValue(false)
                    Timber.i("An error occurred ${e.message}")
                }
        }
    }

    /**
     * This function helps to get cached [WeatherForecast] information from the local db.
     */
    private fun getLocalWeatherForecastData() {
        launch {
            Timber.i("Getting local weather forecast....")
            withContext(Dispatchers.IO) {
                //Get weather forecast from db
                val weatherForecastDb = dao.getAllWeatherForecast()
                weatherForecast.postValue(
                    weatherForecastMapperLocal.transformToDomain(
                        weatherForecastDb
                    )
                )
                weatherForecastIsLoading.postValue(false)
                weatherForecastDataFetchState.postValue(true)
            }
        }
    }

    /**
     * This function is called after fresh [WeatherForecast] from the remote source has been
     * received as it stores the [listOfNetworkWeatherForecast] into the database before updating the UI.
     * @see getRemoteWeatherForecast
     * @param listOfNetworkWeatherForecast the list of [NetworkWeatherForecast] to be converted to a database model and stored.
     */
    private fun storeRemoteForecastDataLocally(listOfNetworkWeatherForecast: List<NetworkWeatherForecast>) {
        launch {
            withContext(Dispatchers.IO) {
                //Empty the db
                dao.deleteAllWeatherForecast()
                //Insert each weather forecast into the db
                for (weatherForecast in listOfNetworkWeatherForecast) {
                    //Get the temperature in kelvin and convert to celsius
                    val kelvinValue = weatherForecast.networkWeatherCondition.temp
                    weatherForecast.networkWeatherCondition.temp =
                        convertKelvinToCelsius(kelvinValue)

                    dao.insertForecastWeather(weatherForecast.toDatabaseModel())
                }

                //Get a list of weather forecast from the db
                val dbForecast = dao.getAllWeatherForecast()

                //Set the value for the weather forecast of type mutable live data
                weatherForecast.postValue(weatherForecastMapperLocal.transformToDomain(dbForecast))
                weatherForecastIsLoading.postValue(false)
                weatherForecastDataFetchState.postValue(true)
            }
        }
        prefHelper.saveTimeOfInitialWeatherForecastFetch(System.nanoTime())
    }

    /**
     * This function helps to get the [Weather] for a particular location [locationName]
     * as provided by the user.
     */
    fun getSearchRemoteWeather(locationName: String) {
        searchWeatherIsLoading.value = true
        launch {
            try {
                val searchWeatherResponse =
                    WeatherApi.retrofitService.getSpecificWeather(locationName, API_KEY)
                searchWeather.postValue(weatherMapperRemote.transformToDomain(searchWeatherResponse))
                searchWeatherIsLoading.postValue(false)
                searchWeatherState.postValue(true)

            } catch (e: Exception) {
                Timber.e("An error occurred $e")
                searchWeatherIsLoading.postValue(false)
                searchWeatherState.postValue(false)
            }
        }
    }

}
