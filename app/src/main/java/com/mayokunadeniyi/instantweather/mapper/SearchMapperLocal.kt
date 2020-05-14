package com.mayokunadeniyi.instantweather.mapper

import com.mayokunadeniyi.instantweather.data.local.entity.DBSearch
import com.mayokunadeniyi.instantweather.data.model.SearchResult

class SearchMapperLocal : BaseMapper<DBSearch, SearchResult> {
    override fun transformToDomain(type: DBSearch): SearchResult = SearchResult(
        uId = type.uId,
        country = type.country,
        name = type.name
    )

    override fun transformToDto(type: SearchResult): DBSearch = DBSearch(
        uId = type.uId,
        country = type.country,
        name = type.name
    )

    fun transFromToDomainList(type: List<DBSearch>): List<SearchResult> {
        return type.map {
            SearchResult(it.uId, it.name, it.country)
        }
    }
}