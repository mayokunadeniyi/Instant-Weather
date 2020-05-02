package com.mayokunadeniyi.instantweather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}