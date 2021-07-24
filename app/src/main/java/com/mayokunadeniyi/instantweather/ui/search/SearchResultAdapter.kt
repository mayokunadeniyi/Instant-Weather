package com.mayokunadeniyi.instantweather.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mayokunadeniyi.instantweather.data.model.SearchResult
import com.mayokunadeniyi.instantweather.databinding.ItemSearchResultBinding

/**
 * Created by Mayokun Adeniyi on 28/04/2020.
 */

class SearchResultAdapter(private val delegate: OnItemClickedListener) : PagedListAdapter<SearchResult, SearchResultAdapter.ViewHolder>(SearchResultDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResult = getItem(position)
        if (searchResult != null) {
            holder.itemView.setOnClickListener {
                delegate.onSearchResultClicked(searchResult)
            }
            holder.bind(searchResult)
        }
    }

    class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResult: SearchResult) {
            binding.searchResult = searchResult
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchResultBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class SearchResultDiffCallBack : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickedListener {
        fun onSearchResultClicked(searchResult: SearchResult)
    }
}
