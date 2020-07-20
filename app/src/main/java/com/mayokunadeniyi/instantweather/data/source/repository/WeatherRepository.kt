package com.mayokunadeniyi.instantweather.data.source.repository

import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.utils.Result

/**
 * Created by Mayokun Adeniyi on 13/07/2020.
 */
interface WeatherRepository {

    suspend fun fetchRemoteWeatherData(location: LocationModel): Result<NetworkWeather>

    suspend fun storeWeatherData(networkWeather: NetworkWeather)

    suspend fun getLocalWeatherData(): DBWeather?

    suspend fun fetchRemoteWeatherForecast(cityId: Int): Result<List<NetworkWeatherForecast>>

    suspend fun getLocalWeatherForecastData(): List<DBWeatherForecast>?

    suspend fun storeForecastData(listOfNetworkWeatherForecast: List<NetworkWeatherForecast>)

    suspend fun getSearchRemoteWeather(locationName: String): Result<NetworkWeather>

    suspend fun deleteWeatherData()

    suspend fun deleteForecastData()

}