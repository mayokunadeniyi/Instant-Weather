package com.mayokunadeniyi.instantweather.utils


import androidx.databinding.BindingAdapter
import com.github.pwittchen.weathericonview.WeatherIconView

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */


@BindingAdapter("setIcon")
fun WeatherIconView.showIcon(condition: String?) {
    val context = this.context
    WeatherIconGenerator.getIconResources(context,this,condition)
}
