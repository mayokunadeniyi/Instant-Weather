package com.mayokunadeniyi.instantweather.utils

import android.view.View

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
inline fun <T: View> T.showIf(condition: (T) -> Boolean){
    visibility = if (condition(this)){
        View.VISIBLE
    }else{
        View.GONE
    }
}
