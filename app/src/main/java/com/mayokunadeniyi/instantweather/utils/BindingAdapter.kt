package com.mayokunadeniyi.instantweather.utils

import androidx.databinding.BindingAdapter
import com.github.pwittchen.weathericonview.WeatherIconView

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */

/**
 * This function helps to dynamically set the icon for the [WeatherIconView]
 * @see WeatherIconGenerator
 */
@BindingAdapter("setIcon")
fun WeatherIconView.showIcon(condition: String?) {
    val context = this.context
    WeatherIconGenerator.getIconResources(context, this, condition)
}
