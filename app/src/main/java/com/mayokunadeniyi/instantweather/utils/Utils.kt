package com.mayokunadeniyi.instantweather.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.text.DecimalFormat

/**
 * Created by Mayokun Adeniyi on 30/03/2020.
 */

/**
 * This function converts a [number] from Kelvin to Celsius by using [DecimalFormat] and
 * converting it to a [Double] then subtracting 273 from it.
 * @param number the number to be converted to Celsius.
 */
fun convertKelvinToCelsius(number: Number): Double {
    return DecimalFormat().run {
        applyPattern(".##")
        parse(format(number.toDouble().minus(273))).toDouble()
    }
}

fun convertCelsiusToFahrenheit(celsius: Double): Double {
    return DecimalFormat().run {
        applyPattern(".##")
        parse(format(celsius.times(1.8).plus(32))).toDouble()
    }
}

/**
 * This functions helps in transforming a [MutableLiveData] of type [T]
 * to a [LiveData] of type [T]
 */
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

/**
 * This function helps to observe a [LiveData] once
 */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(
        lifecycleOwner,
        object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        }
    )
}
