package com.mayokunadeniyi.instantweather.utils

import android.content.Context
import com.github.pwittchen.weathericonview.WeatherIconView
import com.mayokunadeniyi.instantweather.R

/**
 * Created by Mayokun Adeniyi on 27/02/2020.
 */
class WeatherIconGenerator {
    companion object {
        /**
         * This function helps to dynamically set the [WeatherIconView] depending on the weather
         * condition [condition] received.
         * @param iconView the [WeatherIconView] whose icon is to be set
         * @param condition the weather condition
         */
        fun getIconResources(context: Context, iconView: WeatherIconView, condition: String?) {
            if (condition != null) {
                when {
                    condition.contains("rain", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_rain))
                    }
                    condition.contains("snow", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_snow))
                    }
                    condition.contains("sun", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_day_sunny))
                    }
                    condition.contains("cloud", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_forecast_io_cloudy))
                    }
                    condition.contains("Clear", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_wu_clear))
                    }
                    condition.contains("Overcast", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_day_sunny_overcast))
                    }
                    condition.contains("sleet", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_day_sleet_storm))
                    }
                    condition.contains("Mist", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_fog))
                    }
                    condition.contains("drizzle", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_raindrops))
                    }
                    condition.contains("thunderstorm", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_wu_tstorms))
                    }
                    condition.contains("Thunder", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_thunderstorm))
                    }
                    condition.contains("Cloudy", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_forecast_io_cloudy))
                    }
                    condition.contains("Fog", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_forecast_io_fog))
                    }
                    condition.contains("Sunny", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_wu_mostlysunny))
                    }
                    condition.contains("Blizzard", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_snow_wind))
                    }
                    condition.contains("Ice", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_wu_chancesnow))
                    }
                    condition.contains("ice", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_forecast_io_snow))
                    }
                    condition.contains("Rain", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_rain_wind))
                    }
                    condition.contains("wind", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_windy))
                    }
                    condition.contains("Wind", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_strong_wind))
                    }
                    condition.contains("storm", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_storm_warning))
                    }
                    condition.contains("Storm", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_forecast_io_thunderstorm))
                    }
                    condition.contains("thunder", ignoreCase = true) -> {
                        iconView.setIconResource(context.getString(R.string.wi_day_snow_thunderstorm))
                    }
                    else -> {
                        iconView.setIconResource(context.getString(R.string.wi_forecast_io_partly_cloudy_day))
                    }
                }
            }
        }
    }
}
