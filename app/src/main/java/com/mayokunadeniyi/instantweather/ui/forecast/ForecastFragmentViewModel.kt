package com.mayokunadeniyi.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.mapper.WeatherForecastMapperRemote
import com.mayokunadeniyi.instantweather.mapper.toDomain
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.asLiveData
import kotlinx.coroutines.launch

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _forecast = MutableLiveData<List<WeatherForecast>>()
    val forecast = _forecast.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()

    fun getWeatherForecast() {
        _isLoading.value = true
        viewModelScope.launch {
            val localForecast = repository.getLocalWeatherForecastData()
            if (localForecast == null) {
                refreshForecastData()
            } else {
                _forecast.postValue(localForecast.toDomain())
                _isLoading.postValue(false)
            }
        }
    }

    fun refreshForecastData() {
        _isLoading.value = true
        viewModelScope.launch {
            val cityId = repository.getLocalWeatherData()?.cityId
            if (cityId != null) {
                when (val result = repository.fetchRemoteWeatherForecast(cityId)) {
                    is Result.Success -> {
                        _isLoading.value = false
                        if (result.data != null) {
                            _dataFetchState.value = true
                            _forecast.postValue(
                                WeatherForecastMapperRemote().transformToDomain(
                                    result.data
                                )
                            )
                            repository.deleteForecastData()
                            repository.storeForecastData(result.data)
                        } else {
                            _dataFetchState.postValue(false)
                        }

                    }

                    is Result.Error -> {
                        _dataFetchState.value = false
                        _isLoading.value = false
                    }
                }
            } else {
                _isLoading.postValue(false)
                _dataFetchState.postValue(false)
            }
        }
    }

}