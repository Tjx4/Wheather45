package com.platform45.weather45.features.fx

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.models.Conversion
import com.platform45.weather45.models.SeriesDateData
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch

class FxViewModel(application: Application, val fXRepository: FXRepository) : BaseVieModel(application) {
    private val _conversion: MutableLiveData<Conversion?> = MutableLiveData()
    val conversion: MutableLiveData<Conversion?>
        get() = _conversion

    private val _seriesDateData: MutableLiveData<List<SeriesDateData?>?> = MutableLiveData()
    val seriesDateData: MutableLiveData<List<SeriesDateData?>?>
        get() = _seriesDateData

    init {
        ioScope.launch {
            convertCurrency("EUR", "USD", "1")
            getHistorical("2019-03-25-13:00", "EURUSD,USDJPY", "hourly")
            getSeries("2021-04-27", "2021-05-25", "EURUSD", "ohlc")
        }
    }

    suspend fun convertCurrency(from: String, to: String, amount: String) {
        val conversion = fXRepository.getConvertion(API_KEY, from, to , amount)

        uiScope.launch {
            _conversion.value = conversion
        }
    }

    suspend fun getHistorical(date: String, currency: String, interval: String) {
        val historical = fXRepository.getHistorical(API_KEY, date, currency, interval)

            if(historical?.price != null){
                val prices = historical?.price as LinkedTreeMap<String?, Double?>
                val currencies = currency.split(",")
                for(currentCurrency in currencies){
                    val currentPrice = prices[currentCurrency]
                    val dd = currentPrice
                }

                uiScope.launch { }
            }
            else{
                //Handle ex
                uiScope.launch { }
            }

    }

    suspend fun getSeries(startDate: String, endDate: String, currency: String, format: String) {
        val tempseriesDateData = ArrayList<SeriesDateData>()

        val series = fXRepository.getSeries(API_KEY, startDate, endDate, currency, format)
        if(series?.price != null){
            val prices = series?.price as LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>
            for(currentDayPrice in prices){
                val currentDay = currentDayPrice.value as LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>>

                val currencies = currency.split(",")
                for(currentCurrency in currencies){
                    val dateData = currentDay[currentCurrency]
                    val seriesDateData = SeriesDateData(
                        dateData?.get("close")?.toFloat(),
                        dateData?.get("high")?.toFloat(),
                        dateData?.get("low")?.toFloat(),
                        dateData?.get("open")?.toFloat()
                    )
                    tempseriesDateData.add(seriesDateData)
                }
            }

            uiScope.launch {
                _seriesDateData.value = tempseriesDateData
            }
        }
        else{
            //Handle ex
            uiScope.launch { }
        }
    }
}


