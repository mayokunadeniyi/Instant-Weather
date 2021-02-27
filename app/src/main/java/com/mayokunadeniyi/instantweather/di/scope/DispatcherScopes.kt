package com.mayokunadeniyi.instantweather.di.scope

import javax.inject.Qualifier

/**
 * Created by Mayokun Adeniyi on 02/02/2021.
 */

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
