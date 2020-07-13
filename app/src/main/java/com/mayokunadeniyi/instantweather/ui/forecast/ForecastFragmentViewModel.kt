package com.mayokunadeniyi.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.repository.ForecastRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.mayokunadeniyi.instantweather.utils.asLiveData
import kotlinx.coroutines.launch

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel(
    private val repository: ForecastRepository,
    application: Application
) : BaseViewModel(application) {

    val weatherForecast = repository.weatherForecast

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _forecastFetchState = MutableLiveData<Boolean>()
    val forecastFetchState = _forecastFetchState.asLiveData()

    fun getWeatherForecast() = launch {
        _isLoading.value = true
        when (val result = repository.initialForecastFetch()) {
            is Result.Success -> {
                _isLoading.value = false
                _forecastFetchState.value = result.data
            }
            is Result.Error -> {
                _isLoading.value = false
                _forecastFetchState.value = false
            }
        }
    }

    fun refreshForecastData() {
        _isLoading.value = true
        launch {
            when (val result = repository.fetchRemoteWeatherForecast()) {
                is Result.Success -> {
                    _forecastFetchState.value = result.data
                    _isLoading.value = false
                }

                is Result.Error -> {
                    _forecastFetchState.value = false
                    _isLoading.value = false
                }
            }
        }
    }

}