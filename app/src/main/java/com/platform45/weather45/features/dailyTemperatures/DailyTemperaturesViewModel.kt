package com.platform45.weather45.features.dailyTemperatures

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.repositories.WeatherRepository

class DailyTemperaturesViewModel(application: Application, weatherRepository: WeatherRepository) : BaseVieModel(application) {

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: MutableLiveData<String>
        get() = _message

    init {

    }
}