package com.example.instantweather.utils

import android.annotation.SuppressLint
import com.shrikanthravi.collapsiblecalendarview.data.Day
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

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


@SuppressLint("SimpleDateFormat")
fun formatDate(dateString: String): Date?{
    if (dateString.isEmpty().not()){
        return SimpleDateFormat(DATE_FORMAT).parse(dateString)
    }
    return null
}

@SuppressLint("SimpleDateFormat")
fun formatDay(day: String): Date?{
    if (day.isEmpty().not()){
        return SimpleDateFormat(DAY_FORMAT).parse(day)
    }
    return null
}

fun convertDayToString(day: Day):String{
    return "${day.year}-${day.month}-${day.day}"
}