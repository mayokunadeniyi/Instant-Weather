package com.mayokunadeniyi.instantweather.utils

import android.location.Location
import android.view.View
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

inline fun <T: View> T.showIf(condition: (T) -> Boolean){
    visibility = if (condition(this)){
        View.VISIBLE
    }else{
        View.GONE
    }
}
