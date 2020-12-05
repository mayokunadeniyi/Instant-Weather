package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSource
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSourceImpl
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSource
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Force Porquillo on 29/11/2020.
 */

@Suppress("unused")
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun providesWeatherLocalDataSource(
        weatherLocalDataSourceImpl: WeatherLocalDataSourceImpl
    ): WeatherLocalDataSource

    @Binds
    abstract fun providesWeatherRemoteDataSource(
        weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl
    ): WeatherRemoteDataSource
}