package com.example.instantweather.ui.forecast


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.instantweather.databinding.FragmentForecastBinding
import com.example.instantweather.utils.*
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import com.stone.vega.library.VegaLayoutManager
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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

        recyclerView.layoutManager = VegaLayoutManager()
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
                }
            } else {
                binding.apply {
                    forecastProgressBar.visibility = View.GONE
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

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDaySelect() {
                val currentDay = binding.calendarView.selectedDay
                val checker = currentDay?.day!!

                val currentList = weatherForecastAdapter.currentList
                weatherForecastAdapter.submitList(currentList.filter {
                    val format = SimpleDateFormat("yyyy-M-dd HH:mm:ss", Locale.US)
                    val dv = format.parse(it.date)
                    val sec = dv.date
                    sec == checker
                })
                weatherForecastAdapter.notifyDataSetChanged()

            }

            override fun onItemClick(v: View) {
            }

            override fun onMonthChange() {
                Toast.makeText(context,"Month changed",Toast.LENGTH_LONG).show()
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
            }
        }
    }

}
