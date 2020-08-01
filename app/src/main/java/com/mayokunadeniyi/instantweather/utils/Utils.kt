package com.mayokunadeniyi.instantweather.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Mayokun Adeniyi on 30/03/2020.
 */

/**
 * This function converts a [number] from Kelvin to Celsius by converting it to
 * a [Double] then subtracting 273 from it.
 * @param number the number to be converted to Celsius.
 */
fun convertKelvinToCelsius(number: Number): Double {
    return "%.2f".format(number.toDouble().minus(273)).toDouble()
}

/**
 * This function helps to toggle the visibility of a [View]. If the condition
 * is met, the [View] is made visible else it is hidden.
 */
inline fun <T : View> T.showIf(condition: (T) -> Boolean) {
    visibility = if (condition(this)) {
        View.VISIBLE
    } else {
        View.GONE
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
