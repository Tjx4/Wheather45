package com.platform45.weather45.features.history

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.models.DayData
import com.platform45.weather45.models.PairTradeHistory
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.Set
import kotlin.collections.ArrayList

class HistoryViewModel(application: Application, private val fXRepository: FXRepository) : BaseVieModel(application) {
    private val _tradingPair: MutableLiveData<String?> = MutableLiveData()
    val tradingPair: MutableLiveData<String?>
        get() = _tradingPair

    val currencyFromIndex: MutableLiveData<Int> = MutableLiveData()
    val currencyToIndex: MutableLiveData<Int?> = MutableLiveData()

    private val _from: MutableLiveData<String?> = MutableLiveData()
    val from: MutableLiveData<String?>
        get() = _from

    private val _to: MutableLiveData<String?> = MutableLiveData()
    val to: MutableLiveData<String?>
        get() = _to

    private val _currencies: MutableLiveData<List<String>> = MutableLiveData()
    val currencies: MutableLiveData<List<String>>
        get() = _currencies

    private val _pairs: MutableLiveData<List<String?>?> = MutableLiveData()
    val pairs: MutableLiveData<List<String?>?>
        get() = _pairs

    private val _pairTradeHistories: MutableLiveData<List<PairTradeHistory>?> = MutableLiveData()
    val pairTradeHistories: MutableLiveData<List<PairTradeHistory>?>
        get() = _pairTradeHistories

    init {
        setCurrencies()
    }

    fun setCurrencies() {
        val tempList = ArrayList<String>()
        for(currency in Currency.getAvailableCurrencies()){
            tempList.add(currency.currencyCode)
        }
        _currencies.value  = tempList.sortedBy { it }
    }

    fun setCurrencyPair(frmIndx: Int, ToIndx: Int){
        val currencies = _currencies.value
        val currencyFromIndex = currencyFromIndex.value ?: 0
        val currencyToIndex = currencyToIndex.value ?: 0
        val tradingPair = "${currencies?.get(frmIndx)}${currencies?.get(ToIndx)}"
        _tradingPair.value = tradingPair
    }

/*
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
*/


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

                tempPairTrades.add(PairTradeHistory(pairTrade, startDate, endDate, currencyDateData))
            }

            uiScope.launch {
                _pairs.value = currencies
                _pairTradeHistories.value = tempPairTrades
            }
        }
        else{
            //Handle ex
            uiScope.launch { }
        }
    }
}


