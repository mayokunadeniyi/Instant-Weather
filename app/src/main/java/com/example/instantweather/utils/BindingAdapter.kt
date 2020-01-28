package com.example.instantweather.utils


import androidx.databinding.BindingAdapter
import com.example.instantweather.R
import com.github.pwittchen.weathericonview.WeatherIconView

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */


@BindingAdapter("setIcon")
fun WeatherIconView.showIcon(condition: String) {
    val iconView = this
    when {
        condition.contains("rain") -> {
            iconView.setIconResource(context.getString(R.string.wi_rain))
        }
        condition.contains("snow") -> {
            iconView.setIconResource(context.getString(R.string.wi_snow))
        }
        condition.contains("sun") -> {
            iconView.setIconResource(context.getString(R.string.wi_day_sunny))
        }
        condition.contains("cloud") -> {
            iconView.setIconResource(context.getString(R.string.wi_forecast_io_cloudy))
        }
        condition.contains("Clear") -> {
            iconView.setIconResource(context.getString(R.string.wi_wu_clear))
        }
        condition.contains("Overcast") -> {
            iconView.setIconResource(context.getString(R.string.wi_day_sunny_overcast))
        }
        condition.contains("sleet") -> {
            iconView.setIconResource(context.getString(R.string.wi_day_sleet_storm))
        }
        condition.contains("Mist") -> {
            iconView.setIconResource(context.getString(R.string.wi_fog))
        }
        condition.contains("drizzle") -> {
            iconView.setIconResource(context.getString(R.string.wi_raindrops))
        }
        condition.contains("thunderstorm") -> {
            iconView.setIconResource(context.getString(R.string.wi_wu_tstorms))
        }
        condition.contains("Thunder") -> {
            iconView.setIconResource(context.getString(R.string.wi_thunderstorm))
        }
        condition.contains("Cloudy") -> {
            iconView.setIconResource(context.getString(R.string.wi_forecast_io_cloudy))
        }
        condition.contains("Fog") -> {
            iconView.setIconResource(context.getString(R.string.wi_forecast_io_fog))
        }
        condition.contains("Sunny") -> {
            iconView.setIconResource(context.getString(R.string.wi_wu_mostlysunny))
        }
        condition.contains("Blizzard") -> {
            iconView.setIconResource(context.getString(R.string.wi_snow_wind))
        }
        condition.contains("Ice") -> {
            iconView.setIconResource(context.getString(R.string.wi_wu_chancesnow))
        }
        condition.contains("ice") -> {
            iconView.setIconResource(context.getString(R.string.wi_forecast_io_snow))
        }
        condition.contains("Rain") -> {
            iconView.setIconResource(context.getString(R.string.wi_rain_wind))
        }
        condition.contains("wind") -> {
            iconView.setIconResource(context.getString(R.string.wi_windy))
        }
        condition.contains("Wind") -> {
            iconView.setIconResource(context.getString(R.string.wi_strong_wind))
        }
        condition.contains("storm") -> {
            iconView.setIconResource(context.getString(R.string.wi_storm_warning))
        }
        condition.contains("Storm") -> {
            iconView.setIconResource(context.getString(R.string.wi_forecast_io_thunderstorm))
        }
        condition.contains("thunder") -> {
            iconView.setIconResource(context.getString(R.string.wi_day_snow_thunderstorm))
        }
        else -> {
            iconView.setIconResource(context.getString(R.string.wi_forecast_io_partly_cloudy_day))
        }
    }
}
