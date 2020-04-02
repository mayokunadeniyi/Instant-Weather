package com.example.instantweather.ui.forecast


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.instantweather.R

import com.example.instantweather.databinding.FragmentForecastBinding
import com.example.instantweather.databinding.WeatherItemBinding
import com.example.instantweather.ui.MainActivity
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : Fragment() {
    private lateinit var binding: FragmentForecastBinding
    private lateinit var viewModel: ForecastFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater)
        setupCalendar()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.forecastRecyclerview
        val weatherForecastAdapter = WeatherForecastAdapter()

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
            if (state){
                binding.apply {
                    forecastRecyclerview.visibility = View.VISIBLE

                }
            }else{
                binding.apply {
                    forecastRecyclerview.visibility = View.GONE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { state ->
            if (state){
                binding.apply {
                    forecastProgressBar.visibility = View.VISIBLE
                    weatherForecastText.visibility = View.GONE
                }
            }else{
                binding.apply {
                    forecastProgressBar.visibility = View.GONE
                    weatherForecastText.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupCalendar() {
        val calender = binding.calendarView
        calender.setCalendarListener(object : CollapsibleCalendar.CalendarListener{
            override fun onClickListener() {

            }

            override fun onDataUpdate() {

            }

            override fun onDayChanged() {
            }

            override fun onDaySelect() {
                val date = calender.selectedDay
                Toast.makeText(context,"Date is ${date?.day}",Toast.LENGTH_SHORT).show()
            }

            override fun onItemClick(v: View) {
            }

            override fun onMonthChange() {
            }

            override fun onWeekChange(position: Int) {
            }
        })
    }

    fun onPermissionResultForecast(permissionGranted: Boolean){
      if (!permissionGranted){
          binding.apply {
              forecastProgressBar.visibility = View.GONE
              forecastRecyclerview.visibility = View.GONE
              calendarView.visibility = View.GONE
              weatherForecastText.visibility = View.GONE
          }
      }
    }

}
