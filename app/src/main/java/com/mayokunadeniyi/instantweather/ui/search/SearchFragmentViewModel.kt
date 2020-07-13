package com.mayokunadeniyi.instantweather.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.mayokunadeniyi.instantweather.BuildConfig
import com.mayokunadeniyi.instantweather.data.local.WeatherDatabase
import com.mayokunadeniyi.instantweather.data.model.SearchResult
import com.mayokunadeniyi.instantweather.data.model.Weather
import com.mayokunadeniyi.instantweather.data.repository.SearchWeatherRepository
import com.mayokunadeniyi.instantweather.ui.BaseViewModel
import com.mayokunadeniyi.instantweather.utils.ALGOLIA_INDEX_NAME
import com.mayokunadeniyi.instantweather.utils.Result
import com.mayokunadeniyi.instantweather.utils.asLiveData
import kotlinx.coroutines.launch

/**
 * Created by Mayokun Adeniyi on 27/04/2020.
 */

class SearchFragmentViewModel(
    private val repository: SearchWeatherRepository,
    application: Application
) : BaseViewModel(application) {

    private val applicationID = BuildConfig.ALGOLIA_APP_ID
    private val algoliaAPIKey = BuildConfig.ALGOLIA_API_KEY
    private val client = ClientSearch(
        ApplicationID(applicationID),
        APIKey(algoliaAPIKey)
    )
    private val index = client.initIndex(IndexName(ALGOLIA_INDEX_NAME))
    private val searcher = SearcherSingleIndex(index)

    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { hit ->
        SearchResult(
            name = hit.json.getPrimitive("name").content,
            subcountry = hit.json.getPrimitive("subcountry").content,
            country = hit.json.getPrimitive("country").content
        )
    }

    private val pagedListConfig = PagedList.Config.Builder().setPageSize(50).build()
    val locations: LiveData<PagedList<SearchResult>> =
        LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()

    val searchBox = SearchBoxConnectorPagedList(searcher, listOf(locations))
    val stats = StatsConnector(searcher)
    private val connection = ConnectionHandler()

    init {
        connection += searchBox
        connection += stats
    }

    val searchWeather = repository.searchWeather

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _searchWeatherState = MutableLiveData<Boolean>()
    val searchWeatherState = _searchWeatherState.asLiveData()

    /**
     * Gets the [Weather] information for the user selected location[name]
     * @param name value of the location whose [Weather] data is to be fetched.
     */
    fun getSearchWeather(name: String) {
        _isLoading.value = true
        launch {
            when (val result = repository.getSearchRemoteWeather(name)) {
                is Result.Success -> {
                    _isLoading.value = false
                    _searchWeatherState.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _searchWeatherState.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        searcher.cancel()
        connection.disconnect()
    }

}