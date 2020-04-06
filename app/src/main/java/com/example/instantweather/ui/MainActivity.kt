package com.example.instantweather.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.instantweather.R
import com.example.instantweather.databinding.ActivityMainBinding
import com.example.instantweather.ui.home.HomeFragment
import com.example.instantweather.ui.home.LocationLiveData
import com.example.instantweather.utils.ACTIVE_TAB
import com.example.instantweather.utils.LOCATION_REQUEST
import com.example.instantweather.utils.SharedPreferenceHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private lateinit var locationLiveData: LocationLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState != null){
            val pos = savedInstanceState.getInt(ACTIVE_TAB)
            binding.bottomNavBar.setActiveItem(pos)
        }
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(applicationContext)
        locationLiveData = LocationLiveData(this)
        binding.bottomNavBar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                val homeFragment = 0
                val chartFragment = 1
                val settingsFragment = 2
                val navController = NavHostFragment.findNavController(fragment)
                when (pos) {
                    homeFragment -> {
                        navController.navigate(R.id.homeFragment)
                    }

                    chartFragment -> {
                        navController.navigate(R.id.forecastFragment)
                    }

                    settingsFragment -> {
                        navController.navigate(R.id.settingsFragment)
                    }
                }
            }

        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bottomNavActiveTabPos = binding.bottomNavBar.getActiveItem()
        outState.putInt(ACTIVE_TAB,bottomNavActiveTabPos)
    }

    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission")
                    .setMessage("This app needs permission to get your location")
                    .setPositiveButton("Ask me") { dialog, which ->
                        requestLocationPermission()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        notifyHomeFragment(false)
                    }
                    .show()
            } else {
                requestLocationPermission()
            }
        } else {
            notifyHomeFragment(true)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                     setUpLocationListener()
                    notifyHomeFragment(true)

                } else {
                    notifyHomeFragment(false)
                }
            }
        }
    }

    private fun notifyHomeFragment(permissionGranted: Boolean) {
        val activeFragment = fragment.childFragmentManager.primaryNavigationFragment
        if (activeFragment is HomeFragment) {
            activeFragment.onPermissionResultHome(permissionGranted)
        }
    }

//    private fun setUpLocationListener() {
//        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        // for getting the current location update after every 2 seconds with high accuracy
//        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,
//            object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    super.onLocationResult(locationResult)
//                    for (location in locationResult.locations) {
//                    SharedPreferenceHelper.getInstance(this@MainActivity).apply {
//                        Timber.i("The first is ${location.latitude} and ${location.longitude}")
//                        saveLatitude(location.latitude)
//                        saveLongitude(location.longitude)
//                    }
//                    }
//                    // Few more things we can do here:
//                    // For example: Update the location of user on server
//                }
//            },
//            Looper.myLooper()
//        )
//    }
}
