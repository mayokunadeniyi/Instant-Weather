package com.example.instantweather.ui.home


import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.instantweather.databinding.FragmentHomeBinding
import com.example.instantweather.ui.MainActivity
import com.example.instantweather.utils.GpsUtil
import androidx.lifecycle.observe
import com.example.instantweather.utils.GPS_REQUEST
import com.example.instantweather.utils.SharedPreferenceHelper
import com.example.instantweather.utils.showIf
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private var isGpsEnabled = false
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(requireContext())
        GpsUtil(requireContext()).turnGPSOn(object : GpsUtil.OnGpsListener {
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
        invokeLocationAction()
        setUpRefreshLayout()
    }

    private fun invokeLocationAction() {
        when {
            allPermissionsGranted() -> {
                observeViewModels()
            }

            shouldShowRequestPermissionRationale() -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission")
                    .setMessage("This application requires access to your location to function!")
                    .setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            requireActivity().finish()
                        }
                    })
                    .setPositiveButton("Ask me", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            requestPermissions(REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE)
                        }
                    })
                    .show()
            }

            !isGpsEnabled -> {
                //Tell them to enable their location
            }

            else -> {
                requestPermissions(REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGpsEnabled = true
                invokeLocationAction()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            invokeLocationAction()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale() = REQUIRED_PERMISSIONS.all {
        shouldShowRequestPermissionRationale(it)
    }

    private fun observeViewModels() {
        viewModel.getLocationLiveData.observe(viewLifecycleOwner, Observer { location ->
            if (location != null){
                Timber.i("The main location is $location")
                sharedPreferenceHelper.saveLocation(location)
                viewModel.shouldRunAfterLocation()
                observeWeatherViewModel()
            }
        })
        viewModel.dataFetch.observe(viewLifecycleOwner, Observer { state ->
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

        viewModel.loading.observe(viewLifecycleOwner, Observer { state ->
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

    private fun observeWeatherViewModel() {
        viewModel.weather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weather = it
                binding.networkWeatherDescription = it.networkWeatherDescription.first()
            }
        })
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

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val LOCATION_REQUEST_CODE = 123
    }

    //Use this function to optimize the way you're hiding and un-hiding views
//    private fun showViews(state: Boolean) {
//        binding.apply {
//            arrayListOf(
//                weatherInText,
//                weatherDet,
//                separator,
//                dateText,
//                weatherIcon,
//                weatherTemperature,
//                weatherMain
//            ).forEach { it.showIf { state } }
//        }
//    }
}
