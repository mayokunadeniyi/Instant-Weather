package com.example.instantweather.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.instantweather.data.model.LocationModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */

class SharedPreferenceHelper {

    companion object{

        private const val PREF_TIME = "Pref time"
        private const val CITY_ID = "City ID"
        private const val LOCATION = "LOCATION"
        private const val CACHE_DURATION = "pref_cache_duration"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferenceHelper? = null

        fun getInstance(context: Context): SharedPreferenceHelper{
            synchronized(this){
                val _instance = instance
                if (_instance == null){
                    prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    instance = _instance
                }
                return SharedPreferenceHelper()
            }
        }
    }

    fun saveUpdateTime(time: Long){
        prefs?.edit(commit = true){
            putLong(PREF_TIME,time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME,0L)

    fun getCacheDuration() = prefs?.getString("pref_cache_duration","0")


    fun saveCityId(cityId: Int){
        prefs?.edit(commit = true){
            putInt(CITY_ID,cityId)
        }
    }

    fun getCityId() = prefs?.getInt(CITY_ID,0)

    fun saveLocation(location: LocationModel){
        val jsonString = GsonBuilder().create().toJson(location)
        prefs?.edit(commit = true){
            putString(LOCATION,jsonString)
        }
    }

    fun getLocation(): LocationModel?{
        val value = prefs?.getString(LOCATION,null)
        return GsonBuilder().create().fromJson(value,LocationModel::class.java)
    }
}