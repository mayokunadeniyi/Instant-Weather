package com.mayokunadeniyi.instantweather.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.repository.InstantWeatherRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
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
        currentSystemTime()
    }


    /**
     * The [Weather] LiveData from the [repository]
     */
    val weather: LiveData<Weather> = repository.weather

    /**
     * Checks if the [Weather] data from the [repository] is still loading
     */
    val loading: LiveData<Boolean> = repository.weatherIsLoading

    /**
     * Monitors the state of the [Weather] data from the [repository] if there is an error or not.
     */
    val dataFetch: LiveData<Boolean> = repository.weatherDataFetchState

    val time = currentSystemTime()


    //Gets the current system time
    @SuppressLint("SimpleDateFormat")
    fun currentSystemTime(): String{
        val currentTime = System.currentTimeMillis()
        val date = Date(currentTime)
        val dateFormat = SimpleDateFormat("EEEE MMM d, hh:mm aaa")
        return dateFormat.format(date)
    }

    /**
     * This is called after the [location] data has been received.
     * This enables the [Weather] for the [location] to be received.
     */
    fun refreshWeatherData(location: Location){
        repository.refreshWeatherData(location)
    }

    /**
     * This is called when the user swipes down to refresh.
     * This enables the [Weather] for the current [location] to be received.
     */
    fun refreshBypassCache(location: Location?) {
        repository.getRemoteWeatherData(location)
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}