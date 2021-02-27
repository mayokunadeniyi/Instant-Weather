package com.mayokunadeniyi.instantweather.di.component

import android.app.Application
import com.mayokunadeniyi.instantweather.InstantWeatherApplication
import com.mayokunadeniyi.instantweather.di.module.AppModule
import com.mayokunadeniyi.instantweather.di.module.DataSourcesModule
import com.mayokunadeniyi.instantweather.di.module.DatabaseModule
import com.mayokunadeniyi.instantweather.di.module.DispatcherModule
import com.mayokunadeniyi.instantweather.di.module.MainActivityModule
import com.mayokunadeniyi.instantweather.di.module.NetworkModule
import com.mayokunadeniyi.instantweather.di.module.RepositoryModule
import com.mayokunadeniyi.instantweather.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Mayokun Adeniyi on 1/18/21.
 */

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class, DispatcherModule::class, RepositoryModule::class,
        NetworkModule::class, DataSourcesModule::class, DatabaseModule::class, MainActivityModule::class, AppModule::class, ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: InstantWeatherApplication)
}
