package com.example.instantweather.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import com.shrikanthravi.collapsiblecalendarview.data.Day
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mayokun Adeniyi on 30/03/2020.
 */

fun convertKelvinToCelsius(number: Number): Double {
    return "%.2f".format(number.toDouble().minus(273)).toDouble()
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}


fun Day.convertDayToString(): String {
    return  "${this.year}-${this.month}-${this.day}"
}

fun Date.isSame(to: Date): Boolean {
    val sdf = SimpleDateFormat(WEATHER_DATE_FORMAT,Locale.getDefault())
    return sdf.format(this) == sdf.format(to)
}

fun String.year(): Array<Char>{
    val list = this.toMutableList().toTypedArray()
    return list.copyOfRange(0,4)
}

fun String.month(): Array<Char>{
    val list = this.toMutableList().toTypedArray()
    return list.copyOfRange(5,7)
}

fun String.dayForCalendar(): Array<Char>{
    val list = this.toMutableList().toTypedArray()
    return list.copyOfRange(8,list.size)
}

fun String.dayForList(): Array<Char>{
    val list = this.toMutableList().toTypedArray()
    return list.copyOfRange(8,10)
}



fun convertToInt(stuff: Array<Char>): Int{
    val stringBuilder = StringBuilder()

    for (c in stuff){
        stringBuilder.append(c)
    }

    return stringBuilder.toString().toInt()
}