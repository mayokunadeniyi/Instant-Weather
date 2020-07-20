package com.mayokunadeniyi.instantweather.data.source.remote

import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.source.remote.retrofit.WeatherApi
import com.mayokunadeniyi.instantweather.data.source.remote.retrofit.WeatherApiService
import com.mayokunadeniyi.instantweather.utils.API_KEY
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * Created by Mayokun Adeniyi on 13/07/2020.
 */

class WeatherRemoteDataSourceImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val retrofitClient: WeatherApiService = WeatherApi.retrofitService
) : WeatherRemoteDataSource {
    override suspend fun getWeather(location: LocationModel): Result<NetworkWeather> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = retrofitClient.getCurrentWeather(
                    location.latitude, location.longitude, API_KEY
                )
                if (result.isSuccessful) {
                    val networkWeather = result.body()
                    Result.Success(networkWeather)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getWeatherForecast(cityId: Int): Result<List<NetworkWeatherForecast>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = retrofitClient.getWeatherForecast(cityId, API_KEY)
                if (result.isSuccessful) {
                    val networkWeatherForecast = result.body()?.weathers
                    Result.Success(networkWeatherForecast)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getSearchWeather(query: String): Result<NetworkWeather> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = retrofitClient.getSpecificWeather(query, API_KEY)
                if (result.isSuccessful) {
                    val networkWeather = result.body()
                    Result.Success(networkWeather)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

}