package com.example.instantweather.ui.chart


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.instantweather.R
import com.example.instantweather.databinding.FragmentChartBinding

/**
 * A simple [Fragment] subclass.
 */
class ChartFragment : Fragment() {
    private lateinit var binding: FragmentChartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}
