package com.mayokunadeniyi.instantweather.worker

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.repository.ForecastRepository
import com.mayokunadeniyi.instantweather.data.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.utils.*

/**
 * Created by Mayokun Adeniyi on 12/06/2020.
 */

class UpdateWeatherWorker(
    context: Context, params: WorkerParameters, application: Application
) : CoroutineWorker(context, params) {
    private val notificationHelper = NotificationHelper("Weather Update", context)
    private val database = WeatherDatabase.getInstance(context)
    private val weatherRepository: WeatherRepository
    private val forecastRepository: ForecastRepository
    private val sharedPrefs = SharedPreferenceHelper.getInstance(context)

    init {
        weatherRepository = WeatherRepository(database, application)
        forecastRepository = ForecastRepository(database, application)
    }

    override suspend fun doWork(): Result {
        val location = sharedPrefs.getLocation()
        return when (val result = weatherRepository.fetchRemoteWeatherData(location)) {
            is com.mayokunadeniyi.instantweather.utils.Result.Success -> {
                if (result.data) {
                    when (val foreResult = forecastRepository.fetchRemoteWeatherForecast()) {
                        is com.mayokunadeniyi.instantweather.utils.Result.Success -> {
                            if (foreResult.data) {
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