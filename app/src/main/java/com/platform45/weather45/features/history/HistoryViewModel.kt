package com.platform45.weather45.features.history

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap
import com.platform45.weather45.R
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.helpers.getCurrentDate
import com.platform45.weather45.helpers.getDaysAgo
import com.platform45.weather45.models.*
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch
import java.lang.Error
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class HistoryViewModel(application: Application, private val fXRepository: FXRepository) : BaseVieModel(application) {
    val availableCurrencies: MutableLiveData<List<String>> = MutableLiveData()

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _canProceed: MutableLiveData<Boolean> = MutableLiveData()
    val canProceed: MutableLiveData<Boolean>
        get() = _canProceed

    private val _showError: MutableLiveData<String> = MutableLiveData()
    val showError: MutableLiveData<String>
        get() = _showError

    private val _currencyPair: MutableLiveData<String> = MutableLiveData()
    val currencyPair: MutableLiveData<String>
        get() = _currencyPair

    private val _startDate: MutableLiveData<String> = MutableLiveData()
    val startDate: MutableLiveData<String>
        get() = _startDate

    private val _endDate: MutableLiveData<String> = MutableLiveData()
    val endDate: MutableLiveData<String>
        get() = _endDate

    private val _popularCurrencyPairs: MutableLiveData<List<String?>> = MutableLiveData()
    val popularCurrencyPairs: MutableLiveData<List<String?>>
        get() = _popularCurrencyPairs

    private val _currencyPairs: MutableLiveData<List<String>> = MutableLiveData()
    val currencyPairs: MutableLiveData<List<String>>
        get() = _currencyPairs

    private val _pairTradeHistories: MutableLiveData<List<PairTradeHistory>?> = MutableLiveData()
    val pairTradeHistories: MutableLiveData<List<PairTradeHistory>?>
        get() = _pairTradeHistories

    private val _isPairsUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val isPairsUpdated: MutableLiveData<Boolean>
        get() = _isPairsUpdated

    init {
        initCurrencies()
        initStartAndEndDate()
        showLoaderAndGetPopularPairs()
        _currencyPairs.value = ArrayList()
    }

    private fun initStartAndEndDate() {
        _startDate.value = getDaysAgo(5)
        _endDate.value = getCurrentDate()
    }

    fun initCurrencies() {
        availableCurrencies.value = ArrayList()
        for(currency in Currency.getAvailableCurrencies()){
            (availableCurrencies.value as ArrayList<String>)?.add(currency.currencyCode)
        }
        availableCurrencies.value?.sortedBy { it }
    }

    fun showLoaderAndGetPopularPairs(){
        _showLoading.value = true
        ioScope.launch {
            getPopularPairs()
        }
    }

    suspend fun getPopularPairs() {
        val popularCurrencyPairs = fXRepository.getUSDCurrencyPairs(API_KEY)
        uiScope.launch {
            when {
                popularCurrencyPairs == null -> _showError.value = app.getString(R.string.unknown_error)
                popularCurrencyPairs.error != null -> _showError.value = popularCurrencyPairs.error?.info
                else -> handlePopularPairs(popularCurrencyPairs)
            }
        }

    }

    fun handlePopularPairs(popularCurrencyPairs: Currencies){
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
            _showError.value = app.getString(R.string.no_popular_currency_pairs)
        }
    }

    fun setCurrencyPair(frmIndx: Int, ToIndx: Int){
        _currencyPair.value = "${availableCurrencies.value?.get(frmIndx) ?: ""}${availableCurrencies.value?.get(ToIndx)}"
    }

    fun addCreatedPairToList(){
        _currencyPair.value?.let { addCurrencyPairToList(it) }
    }

    fun addPopularPairToList(indx: Int){
        val popularCurrencyPairs = _popularCurrencyPairs.value as ArrayList<String>
        addCurrencyPairToList(popularCurrencyPairs[indx])
    }

    fun addCurrencyPairToList(currencyPair: String){
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.add(currencyPair)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _isPairsUpdated.value = true
    }

    fun deleteCurrencyPairFromList(currencyPair: String){
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.remove(currencyPair)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _isPairsUpdated.value = true
    }

    fun showLoadingAndGetPairSeries(){
        _showLoading.value = true
        ioScope.launch {
            val startDate = _startDate.value ?: ""
            val endDate = _endDate.value ?: ""
            val format = "ohlc"
            var currency = getCurrencyPairsString()

            getCurrencyPairSeries(startDate, endDate, currency, format)
        }
    }

    private fun getCurrencyPairsString(): String{
        var currency = ""
        _currencyPairs.value?.let {
            for((index, pair) in it.withIndex()) {
                currency += if (index > 0) ",$pair" else "$pair"
            }
        }
        return currency
    }

    suspend fun getCurrencyPairSeries(startDate: String, endDate: String, currency: String, format: String) {
        var series = fXRepository.getSeries(API_KEY, startDate, endDate, currency, format)
        uiScope.launch {
            when {
                series == null -> _showError.value = app.getString(R.string.unknown_error)
                series?.error != null -> _showError.value = series.error?.info
                else -> processSeries(series, startDate, endDate)
            }
        }
    }


    fun processSeries(series: Series, startDate: String, endDate: String){
        if (series.price != null) {
            val prices = series?.price as LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>
            val tempPairTrades = ArrayList<PairTradeHistory>()

            val currencies = _currencyPairs.value!!
            for (pairTrade in currencies) {
                val currencyDateData = ArrayList<DayData>()
                for (currentPrice in prices) {
                    val currentDay =
                        currentPrice.value as LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>>

                    val dateData = currentDay[pairTrade]
                    val seriesDateData = DayData(
                        currentPrice.key,
                        dateData?.get("close")?.toFloat(),
                        dateData?.get("high")?.toFloat(),
                        dateData?.get("low")?.toFloat(),
                        dateData?.get("open")?.toFloat()
                    )
                    currencyDateData.add(seriesDateData)
                }
                tempPairTrades.add(
                    PairTradeHistory(
                        pairTrade,
                        startDate,
                        endDate,
                        currencyDateData
                    )
                )
            }
            _pairTradeHistories.value = tempPairTrades
        }
        else{
            _showError.value = app.getString(R.string.no_data_found)
        }
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
}


