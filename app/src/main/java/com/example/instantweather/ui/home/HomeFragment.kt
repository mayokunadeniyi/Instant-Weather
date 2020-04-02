package com.example.instantweather.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instantweather.databinding.FragmentHomeBinding
import com.example.instantweather.ui.MainActivity
import com.example.instantweather.utils.GpsUtil
import com.example.instantweather.utils.SharedPreferenceHelper

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(){

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private var isGpsEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
        (activity as MainActivity).checkLocationPermission()
        GpsUtil(requireContext()).turnGPSOn(object : GpsUtil.OnGpsListener{
            override fun gpsStatus(isGPSEnabled: Boolean) {
                this@HomeFragment.isGpsEnabled = isGPSEnabled
            }
        })
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        hideAllViews(true)
        setUpRefreshLayout()

    }

    private fun setUpRefreshLayout() {
        binding.swipeRefreshId.setOnRefreshListener {
            binding.errorText.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            hideViews()
            viewModel.refreshBypassCache()
            binding.swipeRefreshId.isRefreshing = false
        }
    }


    fun onPermissionResultHome(permissionGranted: Boolean){
        if (permissionGranted){
            hideAllViews(false)
            isGpsEnabled = true
            observeViewModels()
        }
    }


    private fun observeViewModels() {

        viewModel.dataFetch.observe(viewLifecycleOwner, Observer { state ->
            if (state == true) {
                unHideViews()
                binding.errorText.visibility = View.GONE

            } else if (state == false) {
                hideViews()
                binding.apply {
                    errorText.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { state ->
            if (state == true) {
                hideViews()
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    loadingText.visibility = View.VISIBLE
                }
            } else if (state == false) {
                unHideViews()
                binding.apply {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                }
            }
        })

        viewModel.weather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weather = it
                binding.networkWeatherDescription = it.networkWeatherDescription.first()
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
