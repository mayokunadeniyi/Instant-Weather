package com.example.instantweather.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instantweather.BuildConfig
import com.example.instantweather.data.model.CityWeather
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


    private val _cityWeather = MutableLiveData<CityWeather>()
    val cityWeather: LiveData<CityWeather>
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
            Log.i("LOOOOL","LOOOOOOOOO@@R")
            try {
                val weatherApiService = WeatherApi.retrofitService.getCurrentWeather("Lagos",API_KEY)
                Log.i("RESPONSE","WEATHER RESPONSE ${weatherApiService}")
                _loading.postValue(false)
                _cityWeather.value = weatherApiService
            }catch (e: Exception){
                Log.i("Errror",e.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}