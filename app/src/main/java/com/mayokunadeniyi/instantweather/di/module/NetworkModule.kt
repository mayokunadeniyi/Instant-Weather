package com.mayokunadeniyi.instantweather.di.module

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mayokunadeniyi.instantweather.data.source.remote.retrofit.ApiEndPoints.BASE_URL
import com.mayokunadeniyi.instantweather.data.source.remote.retrofit.WeatherApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Force Porquillo on 29/011/2020.
 */

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApiService = retrofit.create(WeatherApiService::class.java)
}
