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
import kotlin.collections.ArrayList

class HistoryViewModel(application: Application, private val fXRepository: FXRepository) : BaseVieModel(application) {
    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _hideLoading: MutableLiveData<Boolean> = MutableLiveData()
    val hideLoading: MutableLiveData<Boolean>
        get() = _hideLoading

    private val _currency: MutableLiveData<String> = MutableLiveData()
    val currency: MutableLiveData<String>
        get() = _currency

    private val _currencyPair: MutableLiveData<String> = MutableLiveData()
    val currencyPair: MutableLiveData<String>
        get() = _currencyPair

    private val _from: MutableLiveData<String> = MutableLiveData()
    val from: MutableLiveData<String>
        get() = _from

    private val _to: MutableLiveData<String> = MutableLiveData()
    val to: MutableLiveData<String>
        get() = _to
    private val _popularCurrencyPairs: MutableLiveData<List<String?>> = MutableLiveData()
    val popularCurrencyPairs: MutableLiveData<List<String?>>
        get() = _popularCurrencyPairs

    private val _currencyPairs: MutableLiveData<List<String>> = MutableLiveData()
    val currencyPairs: MutableLiveData<List<String>>
        get() = _currencyPairs

    private val _requestedPairs: MutableLiveData<List<String?>?> = MutableLiveData()
    val requestedPairs: MutableLiveData<List<String?>?>
        get() = _requestedPairs

    private val _pairTradeHistories: MutableLiveData<List<PairTradeHistory>?> = MutableLiveData()
    val pairTradeHistories: MutableLiveData<List<PairTradeHistory>?>
        get() = _pairTradeHistories

    init {
        _from.value = "0000-00-00"
        _to.value = "0000-00-00"
        _showLoading.value = true
        showLoaderAndGetPopular()
        setCurrencies()
    }

    fun setCurrencies() {
        val tempList = ArrayList<String>()
        for(currency in Currency.getAvailableCurrencies()){
            tempList.add(currency.currencyCode)
        }
        _currencyPairs.value  = tempList.sortedBy { it }
    }

    fun setCurrencyPair(frmIndx: Int, ToIndx: Int){
        val currencies = _currencyPairs.value
        val tradingPair = "${currencies?.get(frmIndx) ?: ""}${currencies?.get(ToIndx)}"
        _currencyPair.value = tradingPair
    }

    fun addCurrentPairTolist(){
        val tradingPair = _currencyPair.value
        setCurrency(tradingPair)
    }

    private fun setCurrency(tradingPair: String?) {
        val currentPairs = _currency.value
        _currency.value = if (currentPairs.isNullOrEmpty()) tradingPair else "$currentPairs,$tradingPair"
    }

    fun addPopularPairToList(indx: Int){
        val popularCurrencyPairs = _popularCurrencyPairs.value as ArrayList<String>
        val tradingPair = popularCurrencyPairs[indx]
        setCurrency(tradingPair)
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

    fun showLoaderAndGetPopular(){
        //Show loader
        ioScope.launch {
            getPopular()
        }
    }

    suspend fun getPopular() {
        val popularCurrencyPairs = fXRepository.getUSDCurrencyPairs(API_KEY)
        uiScope.launch {
            if(!popularCurrencyPairs?.currencies.isNullOrEmpty()){

                val tempList = ArrayList<String>()
                popularCurrencyPairs?.currencies?.forEach { currencyPair ->
                    currencyPair?.key?.let {
                        tempList.add(currencyPair.key!!)
                    }
                }

                _popularCurrencyPairs.value = tempList
            }
            else{
                //No popular
            }
        }


    }

    fun showLoadingAndGetPairSeries(){
        _showLoading.value = true
        ioScope.launch {
            val startDate = if(_from.value.equals("0000-00-00") ) "" else _from.value ?: ""
            val endDate = if(_to.value.equals("0000-00-00")) "" else _to.value ?: ""
            val currency = _currency.value ?: ""
            val format = "ohlc"
            getSeries(startDate, endDate, currency, format)
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

                tempPairTrades.add(PairTradeHistory(pairTrade, startDate, endDate, currencyDateData))
            }

            uiScope.launch {
                _requestedPairs.value = currencies
                _pairTradeHistories.value = tempPairTrades
            }
        }
        else{
            //Handle ex
            uiScope.launch {
                _hideLoading.value = true
            }
        }
    }
}


