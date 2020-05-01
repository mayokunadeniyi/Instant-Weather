package com.example.instantweather.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
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
import com.example.instantweather.BuildConfig
import com.example.instantweather.data.local.WeatherDatabase
import com.example.instantweather.data.model.SearchResult
import com.example.instantweather.data.model.Weather
import com.example.instantweather.data.repository.InstantWeatherRepository
import com.example.instantweather.ui.BaseViewModel
import com.example.instantweather.utils.ALGOLIA_INDEX_NAME
import io.ktor.client.features.logging.LogLevel

/**
 * Created by Mayokun Adeniyi on 27/04/2020.
 */

class SearchFragmentViewModel(application: Application) :
    BaseViewModel(application) {

    private val database = WeatherDatabase.getInstance(getApplication())
    private var repository: InstantWeatherRepository
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
        repository = InstantWeatherRepository(database,application)
        connection += searchBox
        connection += stats
    }

    val searchWeather: LiveData<Weather> = repository.searchWeather
    val isLoading: LiveData<Boolean> = repository.searchWeatherIsLoading
    val searchWeatherState: LiveData<Boolean> = repository.searchWeatherState

    fun getSearchWeather(name: String){
        repository.getSearchRemoteWeather(name)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        searcher.cancel()
        connection.disconnect()
    }


}