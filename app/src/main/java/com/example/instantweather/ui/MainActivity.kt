package com.example.instantweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.instantweather.R
import com.example.instantweather.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.bottomNavBar.setOnItemSelectedListener(object: OnItemSelectedListener{
            override fun onItemSelect(pos: Int) {
                val HOME_FRAGMENT = 0
                val HISTORY_FRAGMENT = 1
                val CHART_FRAGMENT = 2
                val SETTINGS_FRAGMENT = 3
                val navController = NavHostFragment.findNavController(fragment)
                when(pos){
                    HOME_FRAGMENT -> {
                        navController.navigate(R.id.homeFragment)
                    }
                    HISTORY_FRAGMENT -> {
                        navController.navigate(R.id.detailFragment)
                    }

                    CHART_FRAGMENT -> {
                       navController.navigate(R.id.forecastFragment)
                    }

                    SETTINGS_FRAGMENT ->{
                        navController.navigate(R.id.settingsFragment)
                    }
                }
            }

        })
    }


}
