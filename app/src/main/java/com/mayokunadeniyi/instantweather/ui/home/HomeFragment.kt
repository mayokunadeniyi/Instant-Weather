package com.mayokunadeniyi.instantweather.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mayokunadeniyi.instantweather.databinding.FragmentHomeBinding
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.mayokunadeniyi.instantweather.utils.observeOnce
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateRefresh()
        binding.viewModel = viewModel
        hideAllViews(true)
        observeViewModels()
        binding.swipeRefreshId.setOnRefreshListener {
            binding.errorText.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            hideViews()
            initiateRefresh()
            binding.swipeRefreshId.isRefreshing = false
        }
    }

    private fun observeViewModels() {
        viewModel.getWeather().observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weather = it
                binding.networkWeatherDescription = it.networkWeatherDescription.first()
            }
        })

        viewModel.dataFetchState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                true -> {
                    unHideViews()
                    binding.errorText.visibility = View.GONE

                }
                false -> {
                    hideViews()
                    binding.apply {
                        errorText.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        loadingText.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                true -> {
                    hideViews()
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        loadingText.visibility = View.VISIBLE
                    }
                }
                false -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        loadingText.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun initiateRefresh() {
        viewModel.getLocationLiveData().observeOnce(viewLifecycleOwner, Observer { location ->
            if (location != null) {
                viewModel.refreshWeather(location)
            } else {
                hideViews()
                binding.apply {
                    errorText.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                }
            }
        })
    }


    private fun hideViews() {
        binding.apply {
            weatherInText.visibility = View.GONE
            weatherDet.visibility = View.GONE
            separator.visibility = View.GONE
            dateText.visibility = View.GONE
            weatherIcon.visibility = View.GONE
            weatherTemperature.visibility = View.GONE
            weatherMain.visibility = View.GONE
        }
    }

    private fun unHideViews() {
        binding.apply {
            weatherInText.visibility = View.VISIBLE
            weatherDet.visibility = View.VISIBLE
            separator.visibility = View.VISIBLE
            dateText.visibility = View.VISIBLE
            weatherIcon.visibility = View.VISIBLE
            weatherTemperature.visibility = View.VISIBLE
            weatherMain.visibility = View.VISIBLE
        }
    }

    private fun hideAllViews(state: Boolean) {
        if (state) {
            binding.apply {
                weatherDet.visibility = View.GONE
                separator.visibility = View.GONE
                dateText.visibility = View.GONE
                weatherIcon.visibility = View.GONE
                weatherTemperature.visibility = View.GONE
                weatherMain.visibility = View.GONE
                errorText.visibility = View.GONE
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
            }
        }
    }
}

