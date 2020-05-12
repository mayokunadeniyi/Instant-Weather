package com.mayokunadeniyi.instantweather.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by haydar on 12/05/2020.
 */
@Parcelize
data class Sys(
    val country: String
) : Parcelable