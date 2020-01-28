package com.example.instantweather.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.instantweather.BuildConfig
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.local.entity.WeatherResponse
import com.example.instantweather.data.model.CityWeatherDto
import com.example.instantweather.data.model.toDatabaseModel
import com.example.instantweather.data.remote.WeatherApi
import com.example.instantweather.utils.SharedPreferenceHelper
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(application: Application): AndroidViewModel(application) {

    private var prefHelper = SharedPreferenceHelper.getInstance(application)
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val API_KEY = BuildConfig.API_KEY

    //Coroutine Context
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private val dao = WeatherDatabase.getInstance(getApplication()).weatherDao


    private val _cityWeather = MutableLiveData<WeatherResponse>()
    val cityWeatherDto: LiveData<WeatherResponse>
    get() = _cityWeather

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
    get() = _error



    fun refresh(){
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getLocalWeatherData()
        } else {
            getRemoteWeatherData()
        }
    }

    private fun getLocalWeatherData() {
        _loading.value = true
        _error.value = false
        coroutineScope.launch {
            withContext(Dispatchers.IO){
                val weatherData = dao.getWeather()
                weatherDataRetrieved(weatherData)
            }
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cacheDurationInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cacheDurationInt.times(1000 * 1000 * 1000L)
        }catch (e: NumberFormatException){
            Timber.i(e.message)
        }
    }

    fun refreshBypassCache(){
        getRemoteWeatherData()
    }


    private fun getRemoteWeatherData() {
        _loading.value = true
        _error.value = false

        coroutineScope.launch {
            Timber.i("Getting response........")
            try {
                val responseCityWeatherDto = WeatherApi.retrofitService.getCurrentWeather("Lagos",API_KEY)
                Timber.i("WEATHER RESPONSE HAS BEEN RECEIVED......")
                Timber.i("The City ID ${responseCityWeatherDto.cityId}")
                storeRemoteDataLocally(responseCityWeatherDto)

            }catch (e: Exception){
                _error.postValue(true)
                Timber.i("AN ERROR OCCURRED ${e.message}")
            }
        }
    }

    private fun storeRemoteDataLocally(weatherDto: CityWeatherDto) {
        coroutineScope.launch {
            //Empty the db
            dao.deleteAllWeather()

            val weatherResponse = weatherDto.toDatabaseModel()

            dao.insertWeather(weatherResponse)

            weatherDataRetrieved(weatherResponse)

        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    private fun weatherDataRetrieved(weather: WeatherResponse){
        _cityWeather.value = weather
        _loading.value = false
        _error.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}