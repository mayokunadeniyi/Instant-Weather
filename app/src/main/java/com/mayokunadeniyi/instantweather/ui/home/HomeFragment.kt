package com.mayokunadeniyi.instantweather.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.mayokunadeniyi.instantweather.InstantWeatherApplication
import com.mayokunadeniyi.instantweather.R
import com.mayokunadeniyi.instantweather.databinding.FragmentHomeBinding
import com.mayokunadeniyi.instantweather.factory.ViewModelProviderFactory
import com.mayokunadeniyi.instantweather.ui.base.BaseFragment
import com.mayokunadeniyi.instantweather.utils.GPS_REQUEST_CHECK_SETTINGS
import com.mayokunadeniyi.instantweather.utils.GpsUtil
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.mayokunadeniyi.instantweather.utils.convertCelsiusToFahrenheit
import com.mayokunadeniyi.instantweather.utils.observeOnce
import com.mayokunadeniyi.instantweather.worker.UpdateWeatherWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private var isGPSEnabled = false
    private lateinit var prefs: SharedPreferenceHelper

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private val viewModel by lazy {
        ViewModelProvider(this, factory).get(HomeFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPreferenceHelper.getInstance(requireContext())
        GpsUtil(requireContext()).turnGPSOn(object : GpsUtil.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                this@HomeFragment.isGPSEnabled = isGPSEnabled
            }
        })
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_share -> {
                val weatherInformation =
                    "Weather Information In "+binding.weatherInText.text.toString()+"\n"+
                    "Date: "+binding.dateText.text.toString()+"\n"+
                    "Weather Temperature: "+binding.weatherTemperature.text.toString()+"\n"+
                    "Humidity: "+binding.humidityText.text.toString()+"\n"+
                    "Pressure: "+binding.pressureText.text.toString()+"\n"+
                    "Wind Speed: "+binding.windSpeedText.text.toString()
                shareWeatherInformation(weatherInformation)
               true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun invokeLocationAction() {
        when {
            allPermissionsGranted() -> {
                viewModel.getLocationLiveData().observeOnce(
                    viewLifecycleOwner,
                    { location ->
                        if (location != null) {
                            viewModel.getWeather(location)
                            setupWorkManager()
                        }
                    }
                )
            }

            shouldShowRequestPermissionRationale() -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.location_permission))
                    .setMessage(getString(R.string.access_location_message))
                    .setNegativeButton(
                        getString(R.string.no)
                    ) { _, _ -> requireActivity().finish() }
                    .setPositiveButton(
                        getString(R.string.ask_me)
                    ) { _, _ ->
                        requestPermissions(REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE)
                    }
                    .show()
            }

            !isGPSEnabled -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.gps_required_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            else -> {
                requestPermissions(REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.weather_motion_duration_large).toLong()
        }
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
        with(viewModel) {
            weather.observe(viewLifecycleOwner) { weather ->
                weather?.let {
                    prefs.saveCityId(it.cityId)

                    if (prefs.getSelectedTemperatureUnit() == activity?.resources?.getString(R.string.temp_unit_fahrenheit))
                        it.networkWeatherCondition.temp = convertCelsiusToFahrenheit(it.networkWeatherCondition.temp)

                    binding.weather = it
                    binding.networkWeatherDescription = it.networkWeatherDescription.first()
                }
            }

            dataFetchState.observe(viewLifecycleOwner) { state ->
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
            }

            isLoading.observe(viewLifecycleOwner) { state ->
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
            }
        }
    }

    private fun initiateRefresh() {
        viewModel.getLocationLiveData().observeOnce(
            viewLifecycleOwner,
            Observer { location ->
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
            }
        )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
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
                            getString(R.string.enable_gps),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

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

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val LOCATION_REQUEST_CODE = 123
    }

    private fun setupWorkManager() {
        viewModel.getLocationLiveData().observeOnce(
            this,
            Observer {
                prefs.saveLocation(it)
            }
        )
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val weatherUpdateRequest =
            PeriodicWorkRequestBuilder<UpdateWeatherWorker>(6, TimeUnit.HOURS)
                .setConstraints(constraint)
                .setInitialDelay(6, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "Update_weather_worker",
            ExistingPeriodicWorkPolicy.REPLACE, weatherUpdateRequest
        )
    }

    private fun shareWeatherInformation(weatherInformation:String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT,weatherInformation)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
