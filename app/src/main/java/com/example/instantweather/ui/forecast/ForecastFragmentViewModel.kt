package com.example.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.model.WeatherForecast
import com.example.instantweather.data.repository.InstantWeatherRepository
import com.example.instantweather.ui.BaseViewModel
import com.example.instantweather.utils.SharedPreferenceHelper

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel(
    application: Application
) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository
    private val sharedPreferenceHelper: SharedPreferenceHelper
    val cityName: String?

    init {
        repository = InstantWeatherRepository(database,application)
        repository.getRemoteWeatherForecast()
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(application.applicationContext)
        cityName = sharedPreferenceHelper.getCityName()
    }

    val weatherForecast: LiveData<List<WeatherForecast>> = repository.weatherForecast

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}