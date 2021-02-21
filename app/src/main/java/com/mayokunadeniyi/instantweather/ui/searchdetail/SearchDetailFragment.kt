package com.mayokunadeniyi.instantweather.ui.searchdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mayokunadeniyi.instantweather.databinding.FragmentSearchDetailBinding
import com.mayokunadeniyi.instantweather.ui.BaseFragment
import com.mayokunadeniyi.instantweather.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SearchDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchDetailBinding
    private val searchDetailFragmentArgs by navArgs<SearchDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDetailBinding.inflate(layoutInflater)
        val weather = searchDetailFragmentArgs.searchWeatherResult
        val location = searchDetailFragmentArgs.location
        if (weather != null && location != null) {
            binding.weather = weather
            binding.weatherCondition = weather.networkWeatherDescription.first()
            binding.location.text = location
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabClose.setOnClickListener { activity?.onBackPressed() }
    }
}
