package com.example.instantweather.utils


import androidx.databinding.BindingAdapter
import com.github.pwittchen.weathericonview.WeatherIconView
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */


@BindingAdapter("setIcon")
fun WeatherIconView.showIcon(condition: String?) {
    val context = this.context
    WeatherIconGenerator.getIconResources(context,this,condition)
}

fun convertKelvinToCelsius(number: Number): Double{
    return "%.2f".format(number.toDouble().minus(273)).toDouble()
}

