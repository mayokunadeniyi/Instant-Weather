package com.mayokunadeniyi.instantweather.ui.home

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.repository.InstantWeatherRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.LocationLiveData
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository
    private val locationData = LocationLiveData(application)

    init {
        repository = InstantWeatherRepository(database, application)
        currentSystemTime()
    }

    val getLocationLiveData = locationData

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

    fun refreshAfterLocation(location: LocationModel){
        repository.refreshWeatherData(location)
    }

    fun refreshBypassCache(location: LocationModel?) {
        repository.getRemoteWeatherData(location)
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}