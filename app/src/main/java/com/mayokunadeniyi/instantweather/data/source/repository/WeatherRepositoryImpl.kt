package com.mayokunadeniyi.instantweather.data.source.repository

import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecast
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSource
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeather
import com.mayokunadeniyi.instantweather.data.source.local.entity.DBWeatherForecast
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSource
import com.mayokunadeniyi.instantweather.mapper.toDatabaseModel
import com.mayokunadeniyi.instantweather.utils.Result
import kotlinx.coroutines.*

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {

    override suspend fun fetchRemoteWeatherData(location: LocationModel): Result<NetworkWeather> =
        withContext(ioDispatcher) {
            return@withContext remoteDataSource.getWeather(location)
        }

    override suspend fun storeWeatherData(networkWeather: NetworkWeather) =
        withContext(ioDispatcher) {
            localDataSource.saveWeather(networkWeather.toDatabaseModel())
        }

    override suspend fun getLocalWeatherData(): DBWeather? = withContext(ioDispatcher) {
        return@withContext localDataSource.getWeather()
    }

    override suspend fun fetchRemoteWeatherForecast(cityId: Int): Result<List<NetworkWeatherForecast>> =
        withContext(ioDispatcher) {
            return@withContext remoteDataSource.getWeatherForecast(cityId)
        }

    override suspend fun getLocalWeatherForecastData(): List<DBWeatherForecast>? =
        withContext(ioDispatcher) {
            return@withContext localDataSource.getForecastWeather()
        }

    override suspend fun storeForecastData(listOfNetworkWeatherForecast: List<NetworkWeatherForecast>) =
        withContext(ioDispatcher) {
            listOfNetworkWeatherForecast.forEach {
                localDataSource.saveForecastWeather(it.toDatabaseModel())
            }
        }

    override suspend fun getSearchRemoteWeather(locationName: String): Result<NetworkWeather> =
        withContext(ioDispatcher) {
            return@withContext remoteDataSource.getSearchWeather(locationName)
        }

    override suspend fun deleteWeatherData() = withContext(ioDispatcher) {
        localDataSource.deleteWeather()
    }

    override suspend fun deleteForecastData() {
        localDataSource.deleteForecastWeather()
    }

}
