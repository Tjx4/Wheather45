package com.platform45.weather45.features.dailyTemperatures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.platform45.weather45.R
//import org.koin.androidx.viewmodel.ext.android.viewModel

class DailyTemperaturesActivity : AppCompatActivity() {
    // private lateinit var binding: ActivityWeatherBinding
    //private val dailyTemperaturesViewModel: DailyTemperaturesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_temperatures)

        //       binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        //        binding.weatherViewModel = weatherViewModel
        //        binding.lifecycleOwner = this
    }
}