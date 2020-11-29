package com.mayokunadeniyi.instantweather.ui.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.asLiveData
import com.mayokunadeniyi.instantweather.utils.convertKelvinToCelsius
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel
@Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _forecast = MutableLiveData<List<WeatherForecast>?>()
    val forecast = _forecast.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()

    fun getWeatherForecast(cityId: Int?) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getForecastWeather(cityId!!, false)) {
                is Result.Success -> {
                    _isLoading.postValue(false)
                    if (!result.data.isNullOrEmpty()) {
                        val forecasts = result.data
                        _dataFetchState.value = true
                        _forecast.value = forecasts
                    } else {
                        refreshForecastData(cityId)
                    }
                }
                is Result.Loading -> _isLoading.postValue(true)
            }
        }
    }

    fun refreshForecastData(cityId: Int?) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getForecastWeather(cityId!!, true)) {
                is Result.Success -> {
                    _isLoading.postValue(false)
                    if (result.data != null) {
                        val forecast = result.data.apply {
                            forEach {
                                it.networkWeatherCondition.temp =
                                    convertKelvinToCelsius(it.networkWeatherCondition.temp)
                            }
                        }
                        _forecast.postValue(forecast)
                        _dataFetchState.postValue(true)
                        repository.deleteForecastData()
                        repository.storeForecastData(forecast)
                    } else {
                        _dataFetchState.postValue(false)
                        _forecast.postValue(null)
                    }
                }

                is Result.Error -> {
                    _dataFetchState.value = false
                    _isLoading.value = false
                }

                is Result.Loading -> _isLoading.postValue(true)
            }
        }
    }
}
