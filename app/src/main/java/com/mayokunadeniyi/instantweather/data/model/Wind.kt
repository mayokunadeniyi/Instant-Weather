package com.mayokunadeniyi.instantweather.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Mayokun Adeniyi on 16/03/2020.
 */

@Parcelize
data class Wind(
    val speed: Double,
    val deg: Int
) : Parcelable
