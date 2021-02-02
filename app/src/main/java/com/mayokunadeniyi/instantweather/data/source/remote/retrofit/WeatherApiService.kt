package com.mayokunadeniyi.instantweather.data.source.remote.retrofit

import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mayokun Adeniyi on 23/05/2020.
 */
interface WeatherApiService {

    /**
     * This function gets the [NetworkWeather] for the [location] the
     * user searched for.
     */
    @GET("/data/2.5/weather")
    suspend fun getSpecificWeather(
        @Query("q") location: String
    ): Response<NetworkWeather>

    // This function gets the weather information for the user's location.
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<NetworkWeather>

    // This function gets the weather forecast information for the user's location.
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("id") cityId: Int
    ): Response<NetworkWeatherForecastResponse>
}
