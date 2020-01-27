package com.example.instantweather.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.instantweather.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.error.observe(this, Observer { state ->
            if (state){
                binding.errorText.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.humidityId.visibility = View.GONE
                binding.pressureId.visibility = View.GONE
                binding.weatherTemperature.visibility = View.GONE
                binding.pressure.visibility = View.GONE
                binding.humid.visibility = View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { state ->
            if (state){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.cityWeatherDto.observe(this, Observer { cityWeather ->
            binding.cityWeather = cityWeather
        })


    }
}
