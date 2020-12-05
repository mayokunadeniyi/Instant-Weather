package com.mayokunadeniyi.instantweather.di.module

import android.app.Application
import com.mayokunadeniyi.instantweather.InstantWeatherApplication
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Created by Force Porquillo on 29/11/2020.
 */

@Module
class AppModule(
    private val application: InstantWeatherApplication
) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}