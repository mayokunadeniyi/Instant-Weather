package com.example.instantweather.ui.settings


import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.instantweather.R
import com.example.instantweather.utils.SharedPreferenceHelper

/**
 * A simple [PreferenceFragmentCompat] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(requireContext())
        init()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init(){
        val themePreferenceKey = PREFERENCE_KEY_THEME
        val themePreference = findPreference<Preference>(themePreferenceKey)
        val selectedOption = sharedPreferenceHelper.getSelectedThemePref()
        themePreference?.summary = selectedOption

        val cachePreferenceKey = PREFERENCE_KEY_CACHE
        val cachePreference = findPreference<Preference>(cachePreferenceKey)
        val setDuration = sharedPreferenceHelper.getUserSetCacheDuration()
        cachePreference?.summary = setDuration
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        val themePreferenceKey = PREFERENCE_KEY_THEME
        if (key == themePreferenceKey) {
            val themePreference = findPreference<Preference>(themePreferenceKey)
            val selectedOption = sharedPreferenceHelper.getSelectedThemePref()
            themePreference?.summary = selectedOption

            when (selectedOption) {
                getString(R.string.light_theme_value) -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                getString(R.string.dark_theme_value) -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                getString(R.string.auto_battery_value) -> setTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                getString(R.string.follow_system_value) -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        val cachePreferenceKey = PREFERENCE_KEY_CACHE
        if (key == cachePreferenceKey){
            val cachePreference = findPreference<Preference>(cachePreferenceKey)
            val setDuration = sharedPreferenceHelper.getUserSetCacheDuration()
            cachePreference?.summary = setDuration
        }
    }

    private fun setTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object{
        private const val PREFERENCE_KEY_THEME = "theme_key"
        private const val PREFERENCE_KEY_CACHE = "cache_key"
    }

}
