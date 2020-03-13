package com.example.instantweather.ui.forecast


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.instantweather.databinding.FragmentForecastBinding
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ForecastFragment : Fragment() {
    private lateinit var binding: FragmentForecastBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater)
        setupCalendar()
        // Inflate the layout for this fragment
        return binding.root
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

}
