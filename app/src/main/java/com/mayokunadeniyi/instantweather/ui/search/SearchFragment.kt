package com.mayokunadeniyi.instantweather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.item.StatsTextView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.google.android.material.snackbar.Snackbar
import com.mayokunadeniyi.instantweather.R
import com.mayokunadeniyi.instantweather.data.model.SearchResult
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.databinding.FragmentSearchBinding
import com.mayokunadeniyi.instantweather.databinding.FragmentSearchDetailBinding
import com.mayokunadeniyi.instantweather.ui.BaseFragment
import com.mayokunadeniyi.instantweather.utils.BaseBottomSheetDialog
import com.mayokunadeniyi.instantweather.utils.convertKelvinToCelsius
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment(), SearchResultAdapter.OnItemClickedListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchDetailBinding: FragmentSearchDetailBinding
    private val bottomSheetDialog by lazy { BaseBottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme) }
    private val viewModel by viewModels<SearchFragmentViewModel> { viewModelFactoryProvider }
    private val searchResultAdapter by lazy { SearchResultAdapter(this) }
    private val connection = ConnectionHandler()
    private lateinit var searchBoxView: SearchBoxViewAppCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        searchDetailBinding = FragmentSearchDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBoxView = SearchBoxViewAppCompat(binding.searchView)
        searchBoxView.searchView.isIconified = false

        val statsView = StatsTextView(binding.stats)
        connection += viewModel.searchBox.connectView(searchBoxView)
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        searchBoxView.onQuerySubmitted = {
            binding.zeroHits.visibility = View.GONE
            if (it != null && it.isNotEmpty()) {
                viewModel.getSearchWeather(it)
            }
        }

        val recyclerView = binding.locationSearchRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchResultAdapter

        searchDetailBinding.fabClose.setOnClickListener {
            if (bottomSheetDialog.isShowing)
                bottomSheetDialog.dismiss()
        }

        with(viewModel) {

            locations.observe(viewLifecycleOwner) { hits ->
                searchResultAdapter.submitList(hits)
                binding.zeroHits.isVisible = hits.size == 0
            }

            weatherInfo.observe(viewLifecycleOwner) { weather ->
                weather?.let {
                    val formattedWeather = it.apply {
                        this.networkWeatherCondition.temp =
                            convertKelvinToCelsius(this.networkWeatherCondition.temp)
                    }
                    displayWeatherResult(formattedWeather)
                }
            }

            isLoading.observe(viewLifecycleOwner) { state ->
                binding.searchWeatherLoader.isVisible = state
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

    private fun displayWeatherResult(result: Weather) {
        with(searchDetailBinding) {
            weatherCondition = result.networkWeatherDescription.first()
            location.text = result.name
            weather = result
        }

        with(bottomSheetDialog) {
            setCancelable(true)
            setContentView(searchDetailBinding.root)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }

    override fun onSearchResultClicked(searchResult: SearchResult) {
        searchBoxView.setText(searchResult.name)
        viewModel.getSearchWeather(searchResult.name)
    }
}
