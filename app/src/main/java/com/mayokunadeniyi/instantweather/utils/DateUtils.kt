package com.mayokunadeniyi.instantweather.utils

import java.text.SimpleDateFormat

/**
 * Created by Mayokun Adeniyi on 7/24/21.
 */

fun String.formatDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatter = SimpleDateFormat("d MMM y, h:mma")
    return formatter.format(parser.parse(this))
}
