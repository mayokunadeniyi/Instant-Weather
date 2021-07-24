package com.mayokunadeniyi.instantweather.data.source.remote

import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.source.remote.retrofit.WeatherApiService
import com.mayokunadeniyi.instantweather.di.scope.IoDispatcher
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Mayokun Adeniyi on 13/07/2020.
 */

class WeatherRemoteDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: WeatherApiService
) : WeatherRemoteDataSource {
    override suspend fun getWeather(location: LocationModel): Result<NetworkWeather> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getCurrentWeather(
                    location.latitude, location.longitude
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
                val result = apiService.getWeatherForecast(cityId)
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
                val result = apiService.getSpecificWeather(query)
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
