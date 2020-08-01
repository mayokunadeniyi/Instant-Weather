package com.mayokunadeniyi.instantweather.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetworkWeatherDescription(
    val id: Long,
    val main: String?,
    val description: String?,
    val icon: String?
) : Parcelable
