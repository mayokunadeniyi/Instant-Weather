package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.ui.MainActivity
import com.mayokunadeniyi.instantweather.ui.forecast.ForecastFragment
import com.mayokunadeniyi.instantweather.ui.home.HomeFragment
import com.mayokunadeniyi.instantweather.ui.search.SearchFragment
import com.mayokunadeniyi.instantweather.ui.searchdetail.SearchDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Force Porquillo on 29/011/2020.
 */

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [WeatherFragmentModule::class])
    abstract fun injectMainActivityFragments(): MainActivity
}

@Module
abstract class WeatherFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeForecastFragment(): ForecastFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

}