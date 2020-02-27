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

        viewModel.refresh()

        observeViewModels()
        setUpRefreshLayout()

    }

    private fun setUpRefreshLayout() {
        binding.apply {
            swipeRefreshId.setOnRefreshListener {
                this.errorText.visibility = View.GONE
                viewModel?.refreshBypassCache()
            }
            swipeRefreshId.isRefreshing = false
        }
    }

    private fun observeViewModels() {
        viewModel.error.observe(viewLifecycleOwner, Observer { state ->
            if (state){
                binding.errorText.visibility = View.VISIBLE
                hideViews()
            }else{
                binding.errorText.visibility = View.GONE
                unHideViews()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { state ->
            if (state){
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    loadingText.visibility = View.VISIBLE
                    hideViews()
                }
            }else{
                binding.apply {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                    unHideViews()
                }
            }
        })

        viewModel.dbWeather.observe(viewLifecycleOwner, Observer { cityWeather ->
            cityWeather?.let {
                binding.cityWeather = it
                binding.weatherDto = it.networkWeatherDescription.first()
            }
        })
    }


    private fun hideViews(){
        binding.apply {
            weatherIn.visibility = View.GONE
            weatherIcon.visibility = View.GONE
            humidityId.visibility = View.GONE
            pressureId.visibility = View.GONE
            weatherTemperature.visibility = View.GONE
            pressure.visibility = View.GONE
            humid.visibility = View.GONE
            weatherMain.visibility = View.GONE
        }
    }

    private fun unHideViews(){
        binding.apply {
            weatherIn.visibility = View.VISIBLE
            weatherIcon.visibility = View.VISIBLE
            humidityId.visibility = View.VISIBLE
            pressureId.visibility = View.VISIBLE
            weatherTemperature.visibility = View.VISIBLE
            pressure.visibility = View.VISIBLE
            humid.visibility = View.VISIBLE
            weatherMain.visibility = View.VISIBLE
        }
    }
}
