package com.platform45.weather45.features.dailyTemperatures

import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.repositories.WeatherRepository

class DailyTemperaturesViewModel(application: Application, weatherRepository: WeatherRepository) : BaseVieModel(application) {

    private val _location: MutableLiveData<Location> = MutableLiveData()
    val location: MutableLiveData<Location>
        get() = _location

    fun setCurrentLocation(location: Location?) {
        _location.value = location
    }
}