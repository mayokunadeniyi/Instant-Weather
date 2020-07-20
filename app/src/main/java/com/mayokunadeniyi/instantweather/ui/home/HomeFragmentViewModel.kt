package com.mayokunadeniyi.instantweather.ui.home

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.mapper.WeatherMapperRemote
import com.mayokunadeniyi.instantweather.mapper.toDomain
import com.mayokunadeniyi.instantweather.utils.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */
class HomeFragmentViewModel(
    private val repository: WeatherRepository,
    application: Application
) :
    ViewModel() {

    private val locationLiveData = LocationLiveData(application)

    init {
        currentSystemTime()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()

    private val _weather = MutableLiveData<Weather>()
    val weather = _weather.asLiveData()

    val time = currentSystemTime()

    fun getLocationLiveData() = locationLiveData

    /**
     * This is called after the [location] data has been received.
     * This enables the [Weather] for the [location] to be received.
     */
    fun getWeather(location: LocationModel) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val localWeather = repository.getLocalWeatherData()
            if (localWeather == null) {
                refreshWeather(location)
            } else {
                _weather.postValue(localWeather.toDomain())
                _isLoading.postValue(false)
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
        _isLoading.postValue(true)
        viewModelScope.launch {
            when (val result = repository.fetchRemoteWeatherData(location)) {
                is Result.Success -> {
                    _isLoading.value = false
                    if (result.data != null) {
                        val networkWeather = result.data
                        _dataFetchState.value = true
                        _weather.postValue(WeatherMapperRemote().transformToDomain(networkWeather))
                        repository.deleteWeatherData()
                        repository.storeWeatherData(networkWeather)
                    } else {
                        _dataFetchState.postValue(false)
                    }
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _dataFetchState.value = false
                }
            }
        }

    }

    @Suppress("UNCHECKED_CAST")
    class HomeFragmentViewModelFactory(
        private val repository: WeatherRepository,
        private val application: Application
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            (HomeFragmentViewModel(repository, application) as T)
    }
}
