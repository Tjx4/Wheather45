package com.platform45.weather45.features.dailyTemperatures

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platform45.weather45.R
import com.platform45.weather45.base.activities.BaseActivity
import com.platform45.weather45.databinding.ActivityDailyTemperaturesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DailyTemperaturesActivity : BaseActivity() {
    private lateinit var binding: ActivityDailyTemperaturesBinding
    private val dailyTemperaturesViewModel: DailyTemperaturesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_daily_temperatures)
        binding.dailyTemperaturesViewModel = dailyTemperaturesViewModel
        binding.lifecycleOwner = this

        addObservers()
    }

    private fun addObservers() {
        dailyTemperaturesViewModel.message.observe(this, Observer { onMessageUpdated(it) })
    }

    fun onMessageUpdated(message: String){

    }

}