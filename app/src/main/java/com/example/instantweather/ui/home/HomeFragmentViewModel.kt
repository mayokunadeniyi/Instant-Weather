package com.example.instantweather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instantweather.BuildConfig
import com.example.instantweather.data.model.CityWeatherDto
import com.example.instantweather.data.remote.WeatherApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel: ViewModel() {


    private val API_KEY = BuildConfig.API_KEY
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)


    private val _cityWeather = MutableLiveData<CityWeatherDto>()
    val cityWeatherDto: LiveData<CityWeatherDto>
    get() = _cityWeather

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
    get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
    get() = _error


    init {
        getWeatherData()
        _loading.value = true
        _error.value = false
    }

    private fun getWeatherData() {
        coroutineScope.launch {
            Timber.i("Getting response........")
            try {
                val weatherApiService = WeatherApi.retrofitService.getCurrentWeather("Lagos",API_KEY)
                Timber.i("WEATHER RESPONSE ${weatherApiService}")
                Timber.i("The City ID ${weatherApiService.cityId}")
                _loading.postValue(false)
                _cityWeather.value = weatherApiService
            }catch (e: Exception){
                _error.postValue(true)
                Timber.i("AN ERROR OCCURRED ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}