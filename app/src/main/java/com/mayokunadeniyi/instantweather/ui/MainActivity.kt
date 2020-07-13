package com.mayokunadeniyi.instantweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.mayokunadeniyi.instantweather.R
import com.mayokunadeniyi.instantweather.databinding.ActivityMainBinding
import com.mayokunadeniyi.instantweather.ui.home.HomeFragmentViewModel
import com.mayokunadeniyi.instantweather.utils.GPS_REQUEST_CHECK_SETTINGS
import com.mayokunadeniyi.instantweather.utils.GpsUtil
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.mayokunadeniyi.instantweather.utils.observeOnce
import com.mayokunadeniyi.instantweather.worker.UpdateWeatherWorker
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private var isGPSEnabled = false
    private lateinit var prefs: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        prefs = SharedPreferenceHelper.getInstance(this)
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

    @SuppressLint("NewApi")
    private fun invokeLocationAction() {
        when {
            allPermissionsGranted() -> {
                viewModel.getLocationLiveData().observe(this, Observer { location ->
                    if (location != null){
                        viewModel.initialWeatherFetch(location)
                        setupWorkManager()
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

    private fun setupWorkManager() {
        viewModel.getLocationLiveData().observeOnce(this, Observer {
            prefs.saveLocation(it)
        })
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val weatherUpdateRequest =
            PeriodicWorkRequestBuilder<UpdateWeatherWorker>(6, TimeUnit.HOURS)
                .setConstraints(constraint)
                .setInitialDelay(6, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "Update_weather_worker",
            ExistingPeriodicWorkPolicy.REPLACE, weatherUpdateRequest
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    @SuppressLint("NewApi")
    private fun shouldShowRequestPermissionRationale() = REQUIRED_PERMISSIONS.all {
        shouldShowRequestPermissionRationale(it)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
