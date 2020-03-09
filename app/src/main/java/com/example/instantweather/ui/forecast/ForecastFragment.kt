package com.example.instantweather.ui.forecast


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.instantweather.databinding.FragmentForecastBinding

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
        // Inflate the layout for this fragment
        return binding.root
    }


}
