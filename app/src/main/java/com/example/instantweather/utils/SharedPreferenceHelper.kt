package com.example.instantweather.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
/**
 * Created by Mayokun Adeniyi on 2020-01-28.
 */

class SharedPreferenceHelper {

    companion object{

        private const val PREF_TIME = "Pref time"
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

}