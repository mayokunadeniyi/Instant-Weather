package com.mayokunadeniyi.instantweather.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.remote.WeatherApi
import com.mayokunadeniyi.instantweather.mapper.WeatherMapperRemote
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.API_KEY
import com.mayokunadeniyi.instantweather.utils.Result
import timber.log.Timber
import java.io.IOException

/**
 * Created by Mayokun Adeniyi on 05/06/2020.
 */

class SearchWeatherRepository(
    private val database: WeatherDatabase,
    application: Application
) : BaseViewModel(application) {

    private val weatherMapperRemote = WeatherMapperRemote()
    //Weather[Domain Model] exposed to be used in the SearchFragmentViewModel
    val searchWeather = MutableLiveData<Weather>()


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
                searchWeather.postValue(weatherMapperRemote.transformToDomain(searchWeatherResponse!!))
                Result.Success(true)
            } else {
                Result.Success(false)
            }

        } catch (error: IOException) {
            Result.Error(error)
        }
    }
}