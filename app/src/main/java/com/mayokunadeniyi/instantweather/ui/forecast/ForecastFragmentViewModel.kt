package com.mayokunadeniyi.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.repository.ForecastRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import kotlinx.coroutines.launch

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel(
    application: Application
) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: ForecastRepository
    private val sharedPreferenceHelper: SharedPreferenceHelper

    init {
        repository = ForecastRepository(database, application)
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(application.applicationContext)
    }

    val weatherForecast = repository.weatherForecast

    val isLoading = MutableLiveData<Boolean>()

    val forecastFetchState = MutableLiveData<Boolean>()

    fun getWeatherForecast() = launch {
        isLoading.value = true
        when (val result = repository.initialForecastFetch()) {
            is Result.Success -> {
                isLoading.value = false
                forecastFetchState.value = result.data
            }
            is Result.Error -> {
                isLoading.value = false
                forecastFetchState.value = false
            }
        }
    }

    fun refreshForecastData() {
        isLoading.value = true
        launch {
            when (val result = repository.fetchRemoteWeatherForecast()) {
                is Result.Success -> {
                    forecastFetchState.value = result.data
                    isLoading.value = false
                }

                is Result.Error -> {
                    forecastFetchState.value = false
                    isLoading.value = false
                }
            }
        }
    }

}