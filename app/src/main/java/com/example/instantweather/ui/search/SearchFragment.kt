package com.example.instantweather.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.autofill.AutofillValue
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.item.StatsTextView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView

import com.example.instantweather.R
import com.example.instantweather.databinding.FragmentSearchBinding
import com.example.instantweather.ui.search.SearchResultAdapter.SearchResultListener
import com.example.instantweather.utils.SharedPreferenceHelper
import com.example.instantweather.utils.showIf
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchFragmentViewModel
    private lateinit var searchResultAdapter: SearchResultAdapter
    private val connection = ConnectionHandler()
    private lateinit var searchBoxView: SearchBoxViewAppCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchFragmentViewModel::class.java)

        searchBoxView = SearchBoxViewAppCompat(binding.searchView)
        searchBoxView.searchView.isIconified = false

        val statsView = StatsTextView(binding.stats)
        connection += viewModel.searchBox.connectView(searchBoxView)
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        searchResultAdapter = SearchResultAdapter(SearchResultListener { name ->
            searchBoxView.setText(name)
            viewModel.getSearchWeather(name)
            observeViewModel(name)
        })

        searchBoxView.onQuerySubmitted = {
            binding.zeroHits.visibility = View.GONE
            if (it != null && it.isNotEmpty()){
                viewModel.getSearchWeather(it)
                observeViewModel(it)
            }
        }


        val recyclerView = binding.locationSearchRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchResultAdapter

        viewModel.locations.observe(viewLifecycleOwner, Observer { hits ->
            searchResultAdapter.submitList(hits)
            binding.zeroHits.showIf { hits.size == 0 }
        })

    }

    private fun observeViewModel(location: String) {
        viewModel.searchWeather.observe(viewLifecycleOwner, Observer { weather ->
            if (weather != null){
                val action = SearchFragmentDirections.actionSearchFragmentToSearchDetailFragment(weather,location)
                findNavController().navigate(action)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { state ->
            binding.searchWeatherLoader.showIf { state }
        })

        viewModel.searchWeatherState.observe(viewLifecycleOwner, Observer { state ->
            if (!state){
               Snackbar.make(requireView(),"An error occured! Please try again.",Snackbar.LENGTH_LONG).show()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        connection.disconnect()
    }

}
