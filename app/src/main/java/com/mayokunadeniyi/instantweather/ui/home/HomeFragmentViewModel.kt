package com.mayokunadeniyi.instantweather.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.work.Data
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(
    private val repository: WeatherRepository,
    application: Application
) : BaseViewModel(application) {

    private val locationLiveData = LocationLiveData(application)

    init {
        currentSystemTime()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()

    val time = currentSystemTime()

    fun getWeather() = repository.weather
    fun getLocationLiveData() = locationLiveData

    /**
     * This is called after the [location] data has been received.
     * This enables the [Weather] for the [location] to be received.
     */
    fun initialWeatherFetch(location: LocationModel) {
        launch {
            _isLoading.value = true
            when (val result = repository.initialWeatherFetch(location)) {
                is Result.Success -> {
                    _isLoading.value = false
                    _dataFetchState.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _dataFetchState.value = false
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun currentSystemTime(): String {
        val currentTime = System.currentTimeMillis()
        val date = Date(currentTime)
        val dateFormat = SimpleDateFormat("EEEE MMM d, hh:mm aaa")
        return dateFormat.format(date)
    }

    /**
     * This is called when the user swipes down to refresh.
     * This enables the [Weather] for the current [location] to be received.
     */
    fun refreshWeather(location: LocationModel) {
        launch {
            when (val result = repository.fetchRemoteWeatherData(location)) {
                is Result.Success -> {
                    _dataFetchState.value = result.data
                    _isLoading.value = false
                }

                is Result.Error -> {
                    _dataFetchState.value = false
                    _isLoading.value = false
                }
            }
        }

    }
}
