package com.platform45.weather45.features.convertion

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.models.Conversion
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch

class ConversionViewModel(application: Application, val fXRepository: FXRepository) : BaseVieModel(application) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _from: MutableLiveData<String> = MutableLiveData()
    val from: MutableLiveData<String>
        get() = _from

    private val _to: MutableLiveData<String> = MutableLiveData()
    val to: MutableLiveData<String>
        get() = _to

    private val _amount: MutableLiveData<Int> = MutableLiveData()
    val amount: MutableLiveData<Int>
        get() = _amount

    private val _convert: MutableLiveData<Conversion?> = MutableLiveData()
    val convert: MutableLiveData<Conversion?>
        get() = _convert

    init {
        _from.value = "USD"
        _to.value = "ZAR"
        _amount.value = 5
    }

    fun checkAndConvert(){
        _showLoading.value = true
        val from = _from.value ?: ""
        val to = _to.value ?: ""
        val amount = _amount.value ?: 0
        ioScope.launch {
            convertCurrency(from, to, amount)
        }
    }

    suspend fun convertCurrency(from: String, to: String, amount: Int) {
        val conversion = fXRepository.getConversion(API_KEY, from, to , amount.toString())
        uiScope.launch {
            if(conversion != null){
                _convert.value = conversion
            }
        }
    }

}