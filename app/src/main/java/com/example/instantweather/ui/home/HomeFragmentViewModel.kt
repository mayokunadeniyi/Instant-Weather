package com.example.instantweather.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.local.entity.DBWeather
import com.example.instantweather.data.model.Weather
import com.example.instantweather.data.repository.InstantWeatherRepository
import com.example.instantweather.ui.BaseViewModel

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository

    init {
        repository = InstantWeatherRepository(database, application)
        repository.refresh()
    }

    //Weather[Domain Model] from repository layer to be used in the application
    val weather: LiveData<Weather> = repository.weather

    val loading: LiveData<Boolean> = repository.isLoading

    val dataFetch: LiveData<Boolean> = repository.dataFetchState



    fun refreshBypassCache() {
        repository.getRemoteWeatherData()
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}