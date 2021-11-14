package com.mayokunadeniyi.instantweather.di.module

import com.mayokunadeniyi.instantweather.di.scope.DefaultDispatcher
import com.mayokunadeniyi.instantweather.di.scope.IoDispatcher
import com.mayokunadeniyi.instantweather.di.scope.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Mayokun Adeniyi on 02/02/2021.
 */

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
