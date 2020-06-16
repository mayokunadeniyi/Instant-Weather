package com.mayokunadeniyi.instantweather.worker

import android.app.Application
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

/**
 * Created by Mayokun Adeniyi on 16/06/2020.
 */

class MyWorkerFactory(private val application: Application) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            UpdateWeatherWorker::class.java.name -> {
                UpdateWeatherWorker(appContext, workerParameters, application)
            }

            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }

}