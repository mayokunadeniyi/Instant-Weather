package com.mayokunadeniyi.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.LiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.repository.InstantWeatherRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel(
    application: Application
) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository
    private val sharedPreferenceHelper: SharedPreferenceHelper

    init {
        repository = InstantWeatherRepository(database,application)
        repository.refreshWeatherForecastData()
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(application.applicationContext)
    }

    val weatherForecast: LiveData<List<WeatherForecast>> = repository.weatherForecast

    val loading: LiveData<Boolean> = repository.weatherForecastIsLoading
    val forecastFetchState: LiveData<Boolean> = repository.weatherForecastDataFetchState

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun refreshByPassCache(){
        repository.getRemoteWeatherForecast()
    }

}