package com.mayokunadeniyi.instantweather.worker

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mayokunadeniyi.instantweather.data.source.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepositoryImpl
import com.mayokunadeniyi.instantweather.utils.*

/**
 * Created by Mayokun Adeniyi on 12/06/2020.
 */

class UpdateWeatherWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: WeatherRepository
) : CoroutineWorker(context, params) {
    private val notificationHelper = NotificationHelper("Weather Update", context)
    private val sharedPrefs = SharedPreferenceHelper.getInstance(context)


    override suspend fun doWork(): Result {
        val location = sharedPrefs.getLocation()
        return when (val result = repository.fetchRemoteWeatherData(location)) {
            is com.mayokunadeniyi.instantweather.utils.Result.Success -> {
                if (result.data != null) {
                    when (val foreResult =
                        repository.fetchRemoteWeatherForecast(result.data.cityId)) {
                        is com.mayokunadeniyi.instantweather.utils.Result.Success -> {
                            if (foreResult.data != null) {
                                notificationHelper.createNotification()
                                Result.success()
                            } else {
                                Result.failure()
                            }
                        }
                        else -> Result.failure()
                    }
                } else {
                    Result.failure()
                }
            }
            else -> Result.failure()
        }
    }
}