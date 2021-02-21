package com.mayokunadeniyi.instantweather.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.mayokunadeniyi.instantweather.R
import com.mayokunadeniyi.instantweather.databinding.FragmentForecastBinding
import com.mayokunadeniyi.instantweather.ui.BaseFragment
import com.mayokunadeniyi.instantweather.ui.forecast.WeatherForecastAdapter.ForecastOnclickListener
import com.mayokunadeniyi.instantweather.utils.SharedPreferenceHelper
import com.mayokunadeniyi.instantweather.utils.convertCelsiusToFahrenheit
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : BaseFragment() {
    private lateinit var binding: FragmentForecastBinding

    private val viewModel by viewModels<ForecastFragmentViewModel> { viewModelFactoryProvider }

    private lateinit var weatherForecastAdapter: WeatherForecastAdapter

    @Inject
    lateinit var prefs: SharedPreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherForecastAdapter = WeatherForecastAdapter(ForecastOnclickListener())

        setupCalendar()
        binding.forecastRecyclerview.adapter = weatherForecastAdapter
        viewModel.getWeatherForecast(prefs.getCityId())
        observeMoreViewModels()
    }

    private fun observeMoreViewModels() {
        with(viewModel) {
            forecast.observe(viewLifecycleOwner) { weatherForecast ->
                weatherForecast?.let {
                    weatherForecast.forEach {
                        if (prefs.getSelectedTemperatureUnit() == activity?.resources?.getString(R.string.temp_unit_fahrenheit))
                            it.networkWeatherCondition.temp = convertCelsiusToFahrenheit(it.networkWeatherCondition.temp)
                    }
                    weatherForecastAdapter.submitList(it)
                }
            }

            dataFetchState.observe(viewLifecycleOwner) { state ->
                binding.apply {
                    forecastRecyclerview.isVisible = state
                    forecastErrorText?.isVisible = !state
                }
            }

            isLoading.observe(viewLifecycleOwner) { state ->
                binding.forecastProgressBar.isVisible = state
            }
        }

        binding.forecastSwipeRefresh.setOnRefreshListener {
            initiateRefresh()
        }
    }

    private fun initiateRefresh() {
        binding.forecastErrorText?.visibility = View.GONE
        binding.forecastProgressBar.visibility = View.VISIBLE
        binding.forecastRecyclerview.visibility = View.GONE
        viewModel.refreshForecastData(prefs.getCityId())
        binding.forecastSwipeRefresh.isRefreshing = false
    }

    private fun setupCalendar() {
        binding.calendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onClickListener() {
            }

            override fun onDataUpdate() {
            }

            override fun onDayChanged() {
            }

            override fun onDaySelect() {
                val selectedDay = binding.calendarView.selectedDay
                if (selectedDay != null) {
                    val checkerDay = selectedDay.day
                    val checkerMonth = selectedDay.month
                    val checkerYear = selectedDay.year

                    val list = viewModel.forecast.value
                    val filteredList = list?.filter { weatherForecast ->
                        val format = SimpleDateFormat("yyyy-M-dd HH:mm:ss", Locale.US)
                        val formattedDate = format.parse(weatherForecast.date)
                        val weatherForecastDay = formattedDate?.date
                        val weatherForecastMonth = formattedDate?.month
                        val weatherForecastYear = formattedDate?.year
                        // This checks if the selected day, month and year are equal. The year requires an addition of 1900 to get the correct year.
                        weatherForecastDay == checkerDay && weatherForecastMonth == checkerMonth && weatherForecastYear?.plus(
                            1900
                        ) == checkerYear
                    }
                    weatherForecastAdapter.submitList(filteredList)
                    weatherForecastAdapter.currentList
                    binding.emptyListText.isVisible = filteredList!!.isEmpty()
                }
            }

            override fun onItemClick(v: View) {
            }

            override fun onMonthChange() {
            }

            override fun onWeekChange(position: Int) {
            }
        })
    }
}
