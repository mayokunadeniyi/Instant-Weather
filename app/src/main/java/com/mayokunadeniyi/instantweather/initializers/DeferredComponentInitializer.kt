package com.mayokunadeniyi.instantweather.initializers

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class DeferredComponentInitializer(private val app: Application) {

    fun initialize() {
        val componentInitializer = ComponentInitializer(app)
        val initializeMessage = when {
            isColdStart() -> "Deferred initialization from cold start"
            else -> "Deferred initialization from warm start"
        }
        WorkManager.getInstance(app)
            .beginUniqueWork(
                "DeferredInitialization",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<DeferredWorker>()
                    .setInputData(workDataOf(DeferredWorker.KEY_INITIALIZE to initializeMessage))
                    .build()
            )
            .enqueue()
    }

    private fun isColdStart(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.INITIALIZED
    }

    private class DeferredWorker(
        context: Context,
        workerParams: WorkerParameters
    ) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val initializeMessage = inputData.getString(KEY_INITIALIZE)
            initializeMessage?.let {
                Log.d("DeferredInit", it)
            }
            ComponentInitializer(applicationContext).initialize()
            return Result.success()
        }

        companion object {
            const val KEY_INITIALIZE = "KEY_INITIALIZE"
        }
    }

    private class ComponentInitializer(private val app: Application) {
        fun initialize() {
            initCrashlytics()
            initAnalytics()  
        }
    }
}