package com.platform45.weather45.features.fx

import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.models.Conversion
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch

class FxViewModel(application: Application, val fXRepository: FXRepository) : BaseVieModel(application) {

    private val _conversion: MutableLiveData<Conversion?> = MutableLiveData()
    val conversion: MutableLiveData<Conversion?>
        get() = _conversion

    init {

        ioScope.launch {
            convertCurrency("EUR", "USD", "1")
        }
    }

    suspend fun convertCurrency(from: String, to: String, amount: String) {
        val conversion = fXRepository.getConvertion(API_KEY, from, to , amount)

        uiScope.launch {
            _conversion.value = conversion
        }
    }
}