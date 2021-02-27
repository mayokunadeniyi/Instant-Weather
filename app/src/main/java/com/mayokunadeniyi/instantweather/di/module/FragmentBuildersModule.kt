package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.ui.forecast.ForecastFragment
import com.mayokunadeniyi.instantweather.ui.home.HomeFragment
import com.mayokunadeniyi.instantweather.ui.search.SearchFragment
import com.mayokunadeniyi.instantweather.ui.searchdetail.SearchDetailFragment
import com.mayokunadeniyi.instantweather.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Mayokun Adeniyi on 30/01/2021.
 */

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeForecastFragment(): ForecastFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchDetailFragment(): SearchDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}
