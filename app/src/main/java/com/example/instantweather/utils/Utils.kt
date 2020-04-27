package com.example.instantweather.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.shrikanthravi.collapsiblecalendarview.data.Day
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

inline fun <T: View> T.showIf(condition: (T) -> Boolean){
    visibility = if (condition(this)){
        View.VISIBLE
    }else{
        View.GONE
    }
}

/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText():String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}