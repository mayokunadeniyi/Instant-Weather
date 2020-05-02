package com.mayokunadeniyi.instantweather.data.remote

import com.mayokunadeniyi.instantweather.data.model.NetworkWeather
import com.mayokunadeniyi.instantweather.data.model.NetworkWeatherForecastResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mayokun Adeniyi on 2020-01-25.
 */


private val BASE_URL = "http://api.openweathermap.org"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface WeatherApiService {

    @GET("/data/2.5/weather")
    suspend fun getSpecificWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): NetworkWeather

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): NetworkWeather

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String
    ): NetworkWeatherForecastResponse
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}