package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Mayokun Adeniyi on 02/02/2021.
 */

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}