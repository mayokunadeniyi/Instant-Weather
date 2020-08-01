package com.mayokunadeniyi.instantweather.ui.searchdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mayokunadeniyi.instantweather.databinding.FragmentSearchDetailBinding
import com.mayokunadeniyi.instantweather.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SearchDetailFragment : Fragment() {

    private lateinit var binding: FragmentSearchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDetailBinding.inflate(layoutInflater)
        val searchDetailFragmentArgs by navArgs<SearchDetailFragmentArgs>()
        val weather = searchDetailFragmentArgs.searchWeatherResult
        val location = searchDetailFragmentArgs.location
        if (weather != null && location != null) {
            binding.weather = weather
            binding.weatherCondition = weather.networkWeatherDescription.first()
            binding.location.text = location
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.fabClose.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }
}
