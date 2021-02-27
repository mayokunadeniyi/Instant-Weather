package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSource
import com.mayokunadeniyi.instantweather.data.source.local.WeatherLocalDataSourceImpl
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSource
import com.mayokunadeniyi.instantweather.data.source.remote.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Mayokun Adeniyi on 02/02/2021.
 */

@Module
abstract class DataSourcesModule {

    @Binds
    abstract fun bindLocalDataSource(localDataSourceImpl: WeatherLocalDataSourceImpl): WeatherLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource
}
