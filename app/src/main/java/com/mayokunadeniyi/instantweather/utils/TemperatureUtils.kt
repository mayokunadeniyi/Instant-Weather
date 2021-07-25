package com.mayokunadeniyi.instantweather.utils

import java.text.DecimalFormat

/**
 * Created by Mayokun Adeniyi on 7/24/21.
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
