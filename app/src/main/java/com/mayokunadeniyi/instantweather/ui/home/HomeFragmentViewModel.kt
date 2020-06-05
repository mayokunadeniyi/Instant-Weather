package com.mayokunadeniyi.instantweather.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: WeatherRepository

    init {
        repository = WeatherRepository(database, application)
        currentSystemTime()
    }

    val isLoading = MutableLiveData<Boolean>()
    val dataFetchState = MutableLiveData<Boolean>()

    val time = currentSystemTime()


    fun getWeather() = repository.weather

    /**
     * This is called after the [location] data has been received.
     * This enables the [Weather] for the [location] to be received.
     */
    fun initialWeatherFetch(location: Location) {
        launch {
            isLoading.value = true
            when (val result = repository.initialWeatherFetch(location)) {
                is Result.Success -> {
                    dataFetchState.value = result.data
                    isLoading.value = false
                }
                is Result.Error -> {
                    dataFetchState.value = false
                    isLoading.value = false
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
    fun refreshBypassCache(location: Location) {
        launch {
            when (val result = repository.fetchRemoteWeatherData(location)) {
                is Result.Success -> {
                    dataFetchState.value = result.data
                    isLoading.value = false
                }

                is Result.Error -> {
                    dataFetchState.value = false
                    isLoading.value = false
                }
            }
        }

    }
}
