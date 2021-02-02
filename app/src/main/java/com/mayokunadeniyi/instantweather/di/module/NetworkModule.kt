package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.data.source.remote.retrofit.WeatherApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Mayokun Adeniyi on 02/02/2021.
 */


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideInstantWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }
}