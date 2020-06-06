package com.mayokunadeniyi.instantweather.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mayokunadeniyi.instantweather.R
import com.mayokunadeniyi.instantweather.databinding.ActivityMainBinding
import com.mayokunadeniyi.instantweather.ui.home.HomeFragmentViewModel
import com.mayokunadeniyi.instantweather.utils.GPS_REQUEST_CHECK_SETTINGS
import com.mayokunadeniyi.instantweather.utils.GpsUtil
import mumayank.com.airlocationlibrary.AirLocation
import mumayank.com.airlocationlibrary.AirLocation.LocationFailedEnum

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private var airLocation: AirLocation? = null
    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()

        GpsUtil(this).turnGPSOn(object : GpsUtil.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnabled
            }
        })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        viewModel.isLoading.observe(this, Observer { state ->
            if (state) {
                binding.mainErrorText.visibility = View.GONE
                binding.mainProgress.visibility = View.GONE
            }
        })
        binding.mainProgress.visibility = View.VISIBLE
        when {
            allPermissionsGranted() -> {
                airLocation = AirLocation(this, false, true, object : AirLocation.Callbacks {
                    override fun onSuccess(location: Location) {
                        binding.mainProgress.visibility = View.GONE
                        binding.mainErrorText.visibility = View.GONE
                        viewModel.initialWeatherFetch(location)
                    }

                    override fun onFailed(locationFailedEnum: LocationFailedEnum) {
                        binding.mainProgress.visibility = View.GONE
                        binding.mainErrorText.visibility = View.VISIBLE
                        Snackbar.make(
                            binding.root,
                            "Error occurred when trying to get your location!",
                            Snackbar.LENGTH_LONG
                        ).show()
                        when (locationFailedEnum) {

                            LocationFailedEnum.DeviceInFlightMode -> {
                                binding.mainErrorText.text = getString(R.string.flight_mode_error)
                            }
                            LocationFailedEnum.LocationPermissionNotGranted -> {
                                binding.mainErrorText.text =
                                    getString(R.string.location_permission_error)
                            }
                            LocationFailedEnum.HighPrecisionNA_TryAgainPreferablyWithInternet -> {
                                binding.mainErrorText.text =
                                    getString(R.string.highprecision_internet_error)
                            }
                            LocationFailedEnum.LocationOptimizationPermissionNotGranted -> {
                                binding.mainErrorText.text =
                                    getString(R.string.location_optimization_error)
                            }

                            else -> {
                                binding.mainErrorText.text = getString(R.string.general_main_error)
                            }
                        }
                    }
                })
            }

            shouldShowRequestPermissionRationale() -> {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission")
                    .setMessage("This application requires access to your location to function!")
                    .setNegativeButton(
                        "No"
                    ) { _, _ -> finish() }
                    .setPositiveButton(
                        "Ask me"
                    ) { _, _ ->
                        requestPermissions(
                            REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE
                        )
                    }
                    .show()
            }

            !isGPSEnabled -> {
                Snackbar.make(
                    binding.root,
                    "GPS is required for this application to function!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            else -> {
                requestPermissions(
                    REQUIRED_PERMISSIONS,
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLocation?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    GPS_REQUEST_CHECK_SETTINGS -> {
                        isGPSEnabled = true
                        invokeLocationAction()
                    }
                }
            }

            Activity.RESULT_CANCELED -> {
                binding.apply {
                    mainErrorText.visibility = View.VISIBLE
                    mainErrorText.text = "Enable your GPS and restart!"
                    mainProgress.visibility = View.GONE
                }
                when (requestCode) {
                    GPS_REQUEST_CHECK_SETTINGS -> {
                        Snackbar.make(
                            binding.root,
                            "Enable your GPS and restart!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale() = REQUIRED_PERMISSIONS.all {
        shouldShowRequestPermissionRationale(it)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLocation?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            invokeLocationAction()
        }
    }


    private fun setupNavigation() {
        val navController = findNavController(R.id.mainNavFragment)
        setupActionBarWithNavController(navController)
        binding.bottomNavBar.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavFragment).navigateUp()

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val LOCATION_REQUEST_CODE = 123
    }
}
