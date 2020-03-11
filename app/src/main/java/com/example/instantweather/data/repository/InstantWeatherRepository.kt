package com.example.instantweather.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.instantweather.BuildConfig
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.model.NetworkWeather
import com.example.instantweather.data.model.Weather
import com.example.instantweather.data.remote.WeatherApi
import com.example.instantweather.mapper.WeatherMapperLocal
import com.example.instantweather.ui.BaseViewModel
import com.example.instantweather.utils.SharedPreferenceHelper
import com.example.instantweather.mapper.toDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */

class InstantWeatherRepository(
    private val database: WeatherDatabase,
    application: Application
) : BaseViewModel(application) {

    private val mapper = WeatherMapperLocal()
    private var prefHelper = SharedPreferenceHelper.getInstance(application)
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private val API_KEY = BuildConfig.API_KEY
    private val dao = database.weatherDao

    //Weather[Domain Model] exposed to the ViewModel to be used in application
    lateinit var weather: MutableLiveData<Weather>


    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getLocalWeatherData()
        } else {
            getRemoteWeatherData()
        }
    }


    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cacheDurationInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cacheDurationInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            Timber.i(e)
        }

    }

    fun getRemoteWeatherData() {
        launch {
            Timber.i("Getting response........")
            try {
                val networkWeather = WeatherApi.retrofitService.getCurrentWeather("Lagos", API_KEY)
                Timber.i("WEATHER RESPONSE HAS BEEN RECEIVED......")
                Timber.i("The City ID ${networkWeather.cityId}")
                storeRemoteDataLocally(networkWeather)

            } catch (e: Exception) {
                Timber.i("AN ERROR OCCURRED ${e.message}")
            }
        }
    }


    private fun getLocalWeatherData() {
        launch {
            withContext(Dispatchers.IO) {
                //Get the weather from the database
                val dbWeather = dao.getWeather().value

                //Set the value for the MutableLiveData of type Weather
                weather.value = mapper.transform(dbWeather!!)
            }
        }
    }

    private fun storeRemoteDataLocally(networkWeather: NetworkWeather) {
        launch {
            //Empty the db
            dao.deleteAllWeather()

            //Convert the network weather response to a database model
            val weatherResponse = networkWeather.toDatabaseModel()

            //Insert the weather of database model into the db
            dao.insertWeather(weatherResponse)

            //Get the weather from the database
            val dbWeather = dao.getWeather().value

            //Set the value for the MutableLiveData of type Weather
            weather.value = mapper.transform(dbWeather!!)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }


}
