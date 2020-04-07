package com.example.instantweather.ui.forecast


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instantweather.data.model.WeatherForecast

import com.example.instantweather.databinding.FragmentForecastBinding
import com.example.instantweather.utils.convertDayToString
import com.example.instantweather.utils.formatDate
import com.example.instantweather.utils.formatDay
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : Fragment() {
    private lateinit var binding: FragmentForecastBinding
    private lateinit var viewModel: ForecastFragmentViewModel
    private lateinit var weatherForecastAdapter: WeatherForecastAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater)
        weatherForecastAdapter = WeatherForecastAdapter()
        setupCalendar()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.forecastRecyclerview

        recyclerView.adapter = weatherForecastAdapter

        viewModel = ViewModelProviders.of(this).get(ForecastFragmentViewModel::class.java)

        viewModel.weatherForecast.observe(viewLifecycleOwner, Observer { weatherForecast ->
            weatherForecast?.let {
                weatherForecastAdapter.submitList(it)
            }
        })
        observeMoreViewModels()
    }

    private fun observeMoreViewModels() {
        viewModel.forecastDataFetch.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                binding.apply {
                    forecastRecyclerview.visibility = View.VISIBLE

                }
            } else {
                binding.apply {
                    forecastRecyclerview.visibility = View.GONE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                binding.apply {
                    forecastProgressBar.visibility = View.VISIBLE
                    weatherForecastText.visibility = View.GONE
                }
            } else {
                binding.apply {
                    forecastProgressBar.visibility = View.GONE
                    weatherForecastText.visibility = View.VISIBLE
                }
            }
        })
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
                val currentDay = binding.calendarView.selectedDay

                val currentList = weatherForecastAdapter.currentList

                weatherForecastAdapter.submitList(currentList.subList(0,3))

                weatherForecastAdapter.notifyDataSetChanged()

            }

            override fun onItemClick(v: View) {
            }

            override fun onMonthChange() {
            }

            override fun onWeekChange(position: Int) {
            }
        })
    }

    fun onPermissionResultForecast(permissionGranted: Boolean) {
        if (!permissionGranted) {
            binding.apply {
                forecastProgressBar.visibility = View.GONE
                forecastRecyclerview.visibility = View.GONE
                calendarView.visibility = View.GONE
                weatherForecastText.visibility = View.GONE
            }
        }
    }

}
