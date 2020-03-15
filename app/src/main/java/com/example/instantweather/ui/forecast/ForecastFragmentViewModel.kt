package com.example.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.model.WeatherForecast
import com.example.instantweather.data.repository.InstantWeatherRepository
import com.example.instantweather.ui.BaseViewModel

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel(
    application: Application
) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository

    init {
        repository = InstantWeatherRepository(database,application)
        repository.getRemoteWeatherForecast()
    }

    val weatherForecast: LiveData<List<WeatherForecast>> = repository.weatherForecast

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}