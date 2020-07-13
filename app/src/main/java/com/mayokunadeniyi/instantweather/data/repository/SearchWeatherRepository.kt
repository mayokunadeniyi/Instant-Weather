package com.mayokunadeniyi.instantweather.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.remote.WeatherApi
import com.mayokunadeniyi.instantweather.mapper.WeatherMapperRemote
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.API_KEY
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.asLiveData
import timber.log.Timber
import java.io.IOException

/**
 * Created by Mayokun Adeniyi on 05/06/2020.
 */

class SearchWeatherRepository(application: Application) : BaseViewModel(application) {

    private val weatherMapperRemote = WeatherMapperRemote()

    //Weather LiveData exposed to be used in the SearchFragmentViewModel
    private val _searchWeather = MutableLiveData<Weather>()
    val searchWeather = _searchWeather.asLiveData()


    /**
     * This function helps to get the [Weather] for a particular location [locationName]
     * as provided by the user.
     */
    suspend fun getSearchRemoteWeather(locationName: String): Result<Boolean> {
        Timber.i("Getting remote weather for $locationName....")
        return try {
            val result = WeatherApi.retrofitService.getSpecificWeather(locationName, API_KEY)
            if (result.isSuccessful) {
                val searchWeatherResponse = result.body()
                _searchWeather.postValue(weatherMapperRemote.transformToDomain(searchWeatherResponse!!))
                Result.Success(true)
            } else {
                Result.Success(false)
            }

        } catch (error: IOException) {
            Result.Error(error)
        }
    }
}