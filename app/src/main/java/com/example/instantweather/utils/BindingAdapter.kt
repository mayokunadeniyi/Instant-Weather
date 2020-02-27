package com.example.instantweather.utils


import androidx.databinding.BindingAdapter
import com.github.pwittchen.weathericonview.WeatherIconView
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */


@BindingAdapter("setIcon")
fun WeatherIconView.showIcon(condition: String?) {
    Timber.i("The condition is $condition")
    val context = this.context
    WeatherIconGenerator.getIconResources(context,this,condition)
}
