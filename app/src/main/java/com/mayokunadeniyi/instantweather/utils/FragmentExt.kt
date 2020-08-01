package com.mayokunadeniyi.instantweather.utils

import androidx.fragment.app.Fragment
import com.mayokunadeniyi.instantweather.InstantWeatherApplication
import com.mayokunadeniyi.instantweather.ViewModelFactory

/**
 * Created by Mayokun Adeniyi on 20/07/2020.
 */

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository =
        ((requireContext()).applicationContext as InstantWeatherApplication).weatherRepository
    return ViewModelFactory(repository, this)
}
