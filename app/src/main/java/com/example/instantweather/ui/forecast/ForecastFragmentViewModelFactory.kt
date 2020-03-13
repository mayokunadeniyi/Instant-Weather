package com.example.instantweather.ui.forecast

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 * Created by Mayokun Adeniyi on 28/02/2020.
 */

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastFragmentViewModel::class.java)) {
            return ForecastFragmentViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}