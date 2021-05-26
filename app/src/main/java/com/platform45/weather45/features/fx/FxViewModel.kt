package com.platform45.weather45.features.fx

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.models.DayData
import com.platform45.weather45.models.PairTradeHistory
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch

class FxViewModel(application: Application, val fXRepository: FXRepository) : BaseVieModel(application) {
    private val _tradingPair: MutableLiveData<String?> = MutableLiveData()
    val tradingPair: MutableLiveData<String?>
        get() = _tradingPair

    private val _from: MutableLiveData<String?> = MutableLiveData()
    val from: MutableLiveData<String?>
        get() = _from

    private val _to: MutableLiveData<String?> = MutableLiveData()
    val to: MutableLiveData<String?>
        get() = _to

    private val _pairTrades: MutableLiveData<List<PairTradeHistory>?> = MutableLiveData()
    val pairTrades: MutableLiveData<List<PairTradeHistory>?>
        get() = _pairTrades

    init {
        _tradingPair.value = "EURUSD,USDJPY"
        _from.value = "2021-05-10"
        _to.value = "2021-05-25"

        ioScope.launch {
            // convertCurrency("EUR", "USD", "1")
            //getHistorical("2019-03-25-13:00", "EURUSD,USDJPY", "hourly")
            getSeries(_from.value ?: "", _to.value ?: "", _tradingPair.value ?: "", "ohlc")
        }
    }

    suspend fun convertCurrency(from: String, to: String, amount: String) {
        val conversion = fXRepository.getConvertion(API_KEY, from, to , amount)
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
        val series = fXRepository.getSeries(API_KEY, startDate, endDate, currency, format)
        if(series?.price != null){
            val prices = series?.price as LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>

            val tempPairTrades = ArrayList<PairTradeHistory>()
            val currencies = currency.split(",")
            for(pairTrade in currencies){
                val currencyDateData = ArrayList<DayData>()

                for(currentDayPrice in prices) {
                    val currentDay =
                    currentDayPrice.value as LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>>

                    val dateData = currentDay[pairTrade]
                    val seriesDateData = DayData(
                        currentDayPrice.key,
                        dateData?.get("close")?.toFloat(),
                        dateData?.get("high")?.toFloat(),
                        dateData?.get("low")?.toFloat(),
                        dateData?.get("open")?.toFloat()
                    )
                    currencyDateData.add(seriesDateData)
                }

                tempPairTrades.add(PairTradeHistory(pairTrade, currencyDateData))
            }

            uiScope.launch {
                _pairTrades.value = tempPairTrades
            }
        }
        else{
            //Handle ex
            uiScope.launch { }
        }
    }
}


