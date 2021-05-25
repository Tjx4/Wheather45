package com.platform45.weather45.features.fx

import android.location.Location
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platform45.weather45.R
import com.platform45.weather45.base.activities.BaseActivity
import com.platform45.weather45.base.activities.GooglePlayActivity
import com.platform45.weather45.databinding.ActivityFxBinding
import com.platform45.weather45.helpers.showErrorDialog
import com.platform45.weather45.models.Conversion
import org.koin.androidx.viewmodel.ext.android.viewModel

class FxActivity : BaseActivity() {
    private lateinit var binding: ActivityFxBinding
    private val fxViewModel: FxViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fx)
        binding.fxViewModel = fxViewModel
        binding.lifecycleOwner = this

        addObservers()
    }

    private fun addObservers() {
        fxViewModel.conversion.observe(this, Observer { onConversion(it) })
    }

    private fun onConversion(conversion: Conversion?){
        //Fetch location weather
    }

}