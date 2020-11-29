package com.mayokunadeniyi.instantweather.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayokunadeniyi.instantweather.factory.ViewModelKey
import com.mayokunadeniyi.instantweather.factory.ViewModelProviderFactory
import com.mayokunadeniyi.instantweather.ui.forecast.ForecastFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.home.HomeFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.search.SearchFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Force Porquillo on 29/11/2020.
 */

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(
        factory: ViewModelProviderFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ForecastFragmentViewModel::class)
    abstract fun providesForecastFragmentViewModel(
        viewModel: ForecastFragmentViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun providesHomeFragmentViewModel(
        viewModel: HomeFragmentViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel::class)
    abstract fun providesSearchFragmentViewModel(
        viewModel: SearchFragmentViewModel
    ): ViewModel
}