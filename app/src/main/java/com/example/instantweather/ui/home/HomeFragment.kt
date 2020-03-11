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


        observeViewModels()
        setUpRefreshLayout()

    }

    private fun setUpRefreshLayout() {
        binding.errorText.visibility = View.GONE
        binding.swipeRefreshId.setOnRefreshListener {
            viewModel.refreshBypassCache()
            binding.swipeRefreshId.isRefreshing = false
        }
    }

    private fun observeViewModels() {
        viewModel.error.observe(viewLifecycleOwner, Observer { state ->
            if (state == true){
                hideViews()
                binding.apply {
                    errorText.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                }
            }else if (state == false){
                unHideViews()
                binding.errorText.visibility = View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { state ->
            if (state == true){
                hideViews()
                binding.apply {
                    progressBar.visibility = View.VISIBLE
                    loadingText.visibility = View.VISIBLE
                }
            }else if (state == false){
                unHideViews()
                binding.apply {
                    progressBar.visibility = View.GONE
                    loadingText.visibility = View.GONE
                }
            }
        })

        viewModel.weather.observe(viewLifecycleOwner, Observer { weather ->
            weather?.let {
                binding.weather = it
                binding.networkWeatherDescription = it.networkWeatherDescription.first()
            }
        })
    }


    private fun hideViews(){
        binding.apply {
            weatherIn.visibility = View.GONE
            weatherIcon.visibility = View.GONE
            weatherTemperature.visibility = View.GONE
            weatherMain.visibility = View.GONE
        }
    }

    private fun unHideViews(){
        binding.apply {
            weatherIn.visibility = View.VISIBLE
            weatherIcon.visibility = View.VISIBLE
            weatherTemperature.visibility = View.VISIBLE
            weatherMain.visibility = View.VISIBLE
        }
    }
}
