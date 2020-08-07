package com.mayokunadeniyi.instantweather

import com.mayokunadeniyi.instantweather.data.model.LocationModel
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.model.WeatherForecast
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.utils.Result

/**
 * Created by Mayokun Adeniyi on 06/08/2020.
 */

class FakeRepositorySuccess : WeatherRepository {
    /**
     * @param refresh in this case is used to toggle if we want to test for
     * when the data returned is of type [Result.Success] or [Result.Error]
     */
    override suspend fun getWeather(location: LocationModel, refresh: Boolean): Result<Weather?> {
        return Result.Success(fakeWeather)
    }

    /**
     * @param refresh in this case is used to toggle if we want to test for
     * when the data returned is of type [Result.Success] or [Result.Error]
     */
    override suspend fun getForecastWeather(
        cityId: Int,
        refresh: Boolean
    ): Result<List<WeatherForecast>?> {
        return Result.Success(
            listOf(
                fakeWeatherForecast,
                fakeWeatherForecast,
                fakeWeatherForecast,
                fakeWeatherForecast
            )
        )
    }

    override suspend fun getSearchWeather(location: String): Result<Weather?> {
        TODO("Not yet implemented")
    }

    override suspend fun storeWeatherData(weather: Weather) {
        TODO("Not yet implemented")
    }

    override suspend fun storeForecastData(forecasts: List<WeatherForecast>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherData() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteForecastData() {
        TODO("Not yet implemented")
    }
}
