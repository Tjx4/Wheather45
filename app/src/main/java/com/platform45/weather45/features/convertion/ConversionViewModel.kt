package com.platform45.weather45.features.convertion

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.models.Conversion
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch

class ConversionViewModel(application: Application, val fXRepository: FXRepository) : BaseVieModel(application) {

    private val _convert: MutableLiveData<Conversion?> = MutableLiveData()
    val convert: MutableLiveData<Conversion?>
        get() = _convert

    fun checkAndConvert(){
        val from = _convert.value?.from ?: ""
        val to = _convert.value?.to?: ""
        val amount = _convert.value?.price ?: 0.0
        ioScope.launch {
            convertCurrency(from, to, amount)
        }
    }

    suspend fun convertCurrency(from: String, to: String, amount: Double) {
        val conversion = fXRepository.getConvertion(API_KEY, from, to , amount.toString())
        uiScope.launch {
            if(conversion != null){
                _convert.value = conversion
            }
        }
    }

}