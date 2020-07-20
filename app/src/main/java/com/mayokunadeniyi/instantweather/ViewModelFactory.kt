package com.mayokunadeniyi.instantweather

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.mayokunadeniyi.instantweather.data.source.repository.WeatherRepository
import com.mayokunadeniyi.instantweather.ui.forecast.ForecastFragmentViewModel
import com.mayokunadeniyi.instantweather.ui.search.SearchFragmentViewModel

/**
 * Created by Mayokun Adeniyi on 20/07/2020.
 */


/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: WeatherRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(ForecastFragmentViewModel::class.java) ->
                ForecastFragmentViewModel(repository)
            isAssignableFrom(SearchFragmentViewModel::class.java) ->
                SearchFragmentViewModel(repository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}