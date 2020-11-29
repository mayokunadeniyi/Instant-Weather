package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Force Porquillo on 29/11/2020.
 */

@Suppress("unused")
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun providesWeatherRepository(
        repositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
