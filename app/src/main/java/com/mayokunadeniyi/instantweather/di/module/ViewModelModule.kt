package com.mayokunadeniyi.instantweather.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayokunadeniyi.instantweather.ViewModelFactory
import com.mayokunadeniyi.instantweather.di.key.ViewModelKey
import com.mayokunadeniyi.instantweather.ui.forecast.ForecastFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.home.HomeFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.search.SearchFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

/**
 * Created by Mayokun Adeniyi on 02/02/2021.
 */

@InstallIn(SingletonComponent::class)
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun bindHomeFragmentViewModel(viewModel: HomeFragmentViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ForecastFragmentViewModel::class)
    abstract fun bindForecastFragmentViewModel(viewModel: ForecastFragmentViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SearchFragmentViewModel::class)
    abstract fun bindSearchFragmentViewModel(viewModel: SearchFragmentViewModel): ViewModel
}
