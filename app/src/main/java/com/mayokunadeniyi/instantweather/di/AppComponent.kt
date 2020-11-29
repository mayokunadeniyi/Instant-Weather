package com.mayokunadeniyi.instantweather.di

import com.mayokunadeniyi.instantweather.InstantWeatherApplication
import com.mayokunadeniyi.instantweather.di.module.ActivityBuilderModule
import com.mayokunadeniyi.instantweather.di.module.AppModule
import com.mayokunadeniyi.instantweather.di.module.DataSourceModule
import com.mayokunadeniyi.instantweather.di.module.DatabaseModule
import com.mayokunadeniyi.instantweather.di.module.NetworkModule
import com.mayokunadeniyi.instantweather.di.module.RepositoryModule
import com.mayokunadeniyi.instantweather.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Force Porquillo on 29/011/2020.
 */

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        RepositoryModule::class,
        AppModule::class,
        DatabaseModule::class,
        ActivityBuilderModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        DataSourceModule::class
    ]
)
interface AppComponent {
    fun inject(application: InstantWeatherApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: InstantWeatherApplication): Builder
        fun appModule(module: AppModule): Builder
        fun build(): AppComponent
    }
}