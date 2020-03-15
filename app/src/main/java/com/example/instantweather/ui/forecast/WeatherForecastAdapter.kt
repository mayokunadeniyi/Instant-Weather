package com.example.instantweather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instantweather.data.model.NetworkWeatherForecastResponse
import com.example.instantweather.data.model.WeatherForecast
import com.example.instantweather.databinding.WeatherItemBinding

/**
 * Created by Mayokun Adeniyi on 15/03/2020.
 */

class WeatherForecastAdapter: ListAdapter<WeatherForecast,WeatherForecastAdapter.ViewHolder>(WeatherForecastDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherForecast = getItem(position)
        holder.bind(weatherForecast)
    }

    class ViewHolder(private val binding: WeatherItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(weatherForecast: WeatherForecast){
            binding.weatherForecast = weatherForecast
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WeatherItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }


    class WeatherForecastDiffCallBack: DiffUtil.ItemCallback<WeatherForecast>(){
        override fun areItemsTheSame(oldItem: WeatherForecast, newItem: WeatherForecast): Boolean {
            return oldItem.uID == newItem.uID
        }

        override fun areContentsTheSame(
            oldItem: WeatherForecast,
            newItem: WeatherForecast
        ): Boolean {
            return oldItem == newItem
        }
    }
}