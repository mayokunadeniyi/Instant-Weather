package com.mayokunadeniyi.instantweather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.item.StatsTextView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.google.android.material.snackbar.Snackbar
import com.mayokunadeniyi.instantweather.databinding.FragmentSearchBinding
import com.mayokunadeniyi.instantweather.ui.BaseFragment
import com.mayokunadeniyi.instantweather.ui.search.SearchResultAdapter.SearchResultListener
import com.mayokunadeniyi.instantweather.utils.convertKelvinToCelsius
import com.mayokunadeniyi.instantweather.utils.showIf

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchFragmentViewModel> { viewModelFactoryProvider }
    private lateinit var searchResultAdapter: SearchResultAdapter
    private val connection = ConnectionHandler()
    private lateinit var searchBoxView: SearchBoxViewAppCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBoxView = SearchBoxViewAppCompat(binding.searchView)
        searchBoxView.searchView.isIconified = false

        val statsView = StatsTextView(binding.stats)
        connection += viewModel.searchBox.connectView(searchBoxView)
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        searchResultAdapter = SearchResultAdapter(
            SearchResultListener { name ->
                searchBoxView.setText(name)
                viewModel.getSearchWeather(name)
                observeViewModel(name)
            }
        )

        searchBoxView.onQuerySubmitted = {
            binding.zeroHits.visibility = View.GONE
            if (it != null && it.isNotEmpty()) {
                viewModel.getSearchWeather(it)
                observeViewModel(it)
            }
        }

        val recyclerView = binding.locationSearchRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchResultAdapter

        viewModel.locations.observe(viewLifecycleOwner) { hits ->
            searchResultAdapter.submitList(hits)
            binding.zeroHits.showIf { hits.size == 0 }
        }
    }

    private fun observeViewModel(location: String) {
        with(viewModel) {
            weatherInfo.observe(viewLifecycleOwner) { weather ->
                if (weather != null) {
                    val weatherValue = weather.apply {
                        this.networkWeatherCondition.temp =
                            convertKelvinToCelsius(this.networkWeatherCondition.temp)
                    }
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToSearchDetailFragment(
                            weatherValue,
                            location
                        )
                    findNavController().navigate(action)
                }
            }

            isLoading.observe(viewLifecycleOwner) { state ->
                binding.searchWeatherLoader.showIf { state }
            }

            dataFetchState.observe(viewLifecycleOwner) { state ->
                if (!state) {
                    Snackbar.make(
                        requireView(),
                        "An error occurred! Please try again.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }
}
