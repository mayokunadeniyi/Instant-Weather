package com.example.instantweather.ui.home

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.model.Weather
import com.example.instantweather.data.repository.InstantWeatherRepository
import com.example.instantweather.ui.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository

    init {
        repository = InstantWeatherRepository(database, application)
        repository.refreshWeatherData()
        currentSystemTime()
    }

    //Weather[Domain Model] from repository layer to be used in the application
    val weather: LiveData<Weather> = repository.weather

    val loading: LiveData<Boolean> = repository.weatherIsLoading

    val dataFetch: LiveData<Boolean> = repository.weatherDataFetchState

    val time = currentSystemTime()



    @SuppressLint("SimpleDateFormat")
    fun currentSystemTime(): String{
        val currentTime = System.currentTimeMillis()
        val date = Date(currentTime)
        val dateFormat = SimpleDateFormat("EEEE MMM d, hh:mm aaa")
        return dateFormat.format(date)
    }

    fun refreshBypassCache() {
        repository.getRemoteWeatherData()
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}