package com.platform45.weather45.features.dailyTemperatures

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platform45.weather45.R
import com.platform45.weather45.base.activities.GooglePlayActivity
import com.platform45.weather45.databinding.ActivityDailyTemperaturesBinding
import com.platform45.weather45.helpers.showErrorDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class DailyTemperaturesActivity : GooglePlayActivity() {
    private lateinit var binding: ActivityDailyTemperaturesBinding
    private val dailyTemperaturesViewModel: DailyTemperaturesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_daily_temperatures)
        binding.dailyTemperaturesViewModel = dailyTemperaturesViewModel
        binding.lifecycleOwner = this

        addObservers()
        checkPlayServicesAndPermission()
    }

    override fun onLocationPermissionDenied() {
        showErrorDialog(
            this,
            getString(R.string.error),
            getString(R.string.permission_denied, "(Location permission),"),
            getString(R.string.close)
        ) {
            finish()
        }
    }

    override fun onLocationRequestListenerSuccess(location: Location?) {
        //Proceed
        Toast.makeText(this, "We at ${location?.latitude}, ${location?.longitude}", Toast.LENGTH_SHORT).show()
    }

    private fun addObservers() {
        dailyTemperaturesViewModel.message.observe(this, Observer { onMessageUpdated(it) })
    }

    private fun onMessageUpdated(message: String){

    }

}