package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.di.module.FragmentBuildersModule
import com.mayokunadeniyi.instantweather.di.scope.PerActivity
import com.mayokunadeniyi.instantweather.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Mayokun Adeniyi on 1/18/21.
 */

@Module
abstract class MainActivityModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}