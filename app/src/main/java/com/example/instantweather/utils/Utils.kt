package com.example.instantweather.utils

/**
 * Created by Mayokun Adeniyi on 30/03/2020.
 */

fun convertKelvinToCelsius(number: Number): Double{
    return "%.2f".format(number.toDouble().minus(273)).toDouble()
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}