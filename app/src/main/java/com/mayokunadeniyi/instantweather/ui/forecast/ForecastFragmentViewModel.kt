package com.mayokunadeniyi.instantweather.ui.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.di.scope.DefaultDispatcher
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.asLiveData
import com.mayokunadeniyi.instantweather.utils.convertKelvinToCelsius
import com.mayokunadeniyi.instantweather.utils.formatDate
import com.shrikanthravi.collapsiblecalendarview.data.Day
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

class ForecastFragmentViewModel @Inject constructor(
    private val repository: WeatherRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _forecast = MutableLiveData<List<WeatherForecast>?>()
    val forecast = _forecast.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()

    private val _filteredForecast = MutableLiveData<List<WeatherForecast>>()
    val filteredForecast = _filteredForecast.asLiveData()

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
                        val forecast = result.data.onEach { forecast ->
                            forecast.networkWeatherCondition.temp =
                                convertKelvinToCelsius(forecast.networkWeatherCondition.temp)
                            forecast.date = forecast.date.formatDate()
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

    fun updateWeatherForecast(selectedDay: Day, list: List<WeatherForecast>) {
        viewModelScope.launch(defaultDispatcher) {
            selectedDay.let {
                val checkerDay = it.day
                val checkerMonth = it.month
                val checkerYear = it.year

                val filteredList = list.filter { weatherForecast ->
                    val format = SimpleDateFormat("d MMM y, h:mma", Locale.ENGLISH)
                    val formattedDate = format.parse(weatherForecast.date)
                    val weatherForecastDay = formattedDate?.date
                    val weatherForecastMonth = formattedDate?.month
                    val weatherForecastYear = formattedDate?.year
                    // This checks if the selected day, month and year are equal. The year requires an addition of 1900 to get the correct year.
                    weatherForecastDay == checkerDay && weatherForecastMonth == checkerMonth && weatherForecastYear?.plus(
                        1900
                    ) == checkerYear
                }
                _filteredForecast.postValue(filteredList)
            }
        }
    }
}
