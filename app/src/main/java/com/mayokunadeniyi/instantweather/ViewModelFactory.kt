package com.mayokunadeniyi.instantweather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Mayokun Adeniyi on 20/07/2020.
 */

/**
 * Factory for all ViewModels.
 * reference : https://github.com/googlesamples/android-architecture-components
 */
@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var viewModel = viewModelMap[modelClass]

        if (viewModel == null) {
            for (entry in viewModelMap) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    viewModel = entry.value
                    break
                }
            }
        }

        if (viewModel == null) throw IllegalArgumentException("Unknown model class $modelClass")
        return viewModel.get() as T
    }
}
