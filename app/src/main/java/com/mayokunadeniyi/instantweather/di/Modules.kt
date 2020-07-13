package com.mayokunadeniyi.instantweather.di

import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.repository.ForecastRepository
import com.mayokunadeniyi.instantweather.data.repository.SearchWeatherRepository
import com.mayokunadeniyi.instantweather.data.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.ui.forecast.ForecastFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.home.HomeFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.search.SearchFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single { WeatherDatabase.getInstance(androidApplication().applicationContext) }
}

val repositoryModule = module {
    single { ForecastRepository(get(), androidApplication()) }
    single { WeatherRepository(get(), androidApplication()) }
    single { SearchWeatherRepository(androidApplication()) }
}

val viewModelModule = module(override = true) {
    viewModel {
        ForecastFragmentViewModel(get(), androidApplication())
    }
    viewModel {
        HomeFragmentViewModel(get(), androidApplication())
    }
    viewModel {
        SearchFragmentViewModel(get(), androidApplication())
    }
}