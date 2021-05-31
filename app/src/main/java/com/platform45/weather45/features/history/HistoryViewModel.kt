package com.platform45.weather45.features.history

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.weather45.R
import com.platform45.weather45.base.viewmodels.BaseVieModel
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.features.history.paging.HistoryState
import com.platform45.weather45.features.history.paging.HistoryEvent
import com.platform45.weather45.features.history.paging.PairPagingSource
import com.platform45.weather45.helpers.getCurrentDate
import com.platform45.weather45.helpers.getDaysAgo
import com.platform45.weather45.helpers.getPairHistoryList
import com.platform45.weather45.models.*
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable
import com.platform45.weather45.repositories.FXRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HistoryViewModel(val app: Application, val fXRepository: FXRepository) : BaseVieModel(app) {
    val availableCurrencies: MutableLiveData<List<String>> = MutableLiveData()

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: MutableLiveData<String>
        get() = _message

    private val _pairsMessage: MutableLiveData<String> = MutableLiveData()
    val pairsMessage: MutableLiveData<String>
        get() = _pairsMessage

    private val _loadRemote: MutableLiveData<Boolean> = MutableLiveData()
    val loadRemote: MutableLiveData<Boolean>
        get() = _loadRemote

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

    private val _pairTradeHistories: MutableLiveData<List<PairTradeHistory?>?> = MutableLiveData()
    val pairTradeHistories: MutableLiveData<List<PairTradeHistory?>?>
        get() = _pairTradeHistories

    private val _isPairsUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val isPairsUpdated: MutableLiveData<Boolean>
        get() = _isPairsUpdated

    init {
        initCurrencies()
        initStartAndEndDate()
        initCurrencyPairs()
    }

    private fun initStartAndEndDate() {
        _startDate.value = getDaysAgo(30)
        _endDate.value = getCurrentDate()
    }

    private fun initCurrencyPairs() {
        _message.value = app.getString(R.string.no_requested_pairs)
        _currencyPairs.value = ArrayList()
    }

    fun initCurrencies() {
        val tmpList = ArrayList<String>()
        for (currency in Currency.getAvailableCurrencies()) {
            tmpList.add(currency.currencyCode)
        }
        availableCurrencies.value = tmpList?.sortedBy { it }

        ioScope.launch {
            getPopularPairs()
        }
    }

    fun checkState() {
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
    }


    /*
    fun checkAndLoad() {
        ioScope.launch {
            val cachedHistories = fXRepository.getAllPairHistoriesFromDb()
            uiScope.launch {
                if (cachedHistories.isNullOrEmpty()) {
                    _loadRemote.value = true
                }
                else{
                    val tmpPairs = ArrayList<String>()
                    cachedHistories.forEach { it?.tradingPair?.let { tp -> tmpPairs.add(tp)} }
                    _currencyPairs.value = tmpPairs
                    _pairTradeHistories.value = cachedHistories
                }
            }
        }
    }
*/
    suspend fun getPopularPairs() {
        val popularCurrencyPairs = fXRepository.getPopularCurrencyPairs(API_KEY)
        uiScope.launch {
            when {
                popularCurrencyPairs == null -> _showError.value =
                    app.getString(R.string.unknown_error)
                popularCurrencyPairs.error != null -> _showError.value =
                    popularCurrencyPairs.error?.info
                else -> handlePopularPairs(popularCurrencyPairs)
            }
        }

    }

    fun handlePopularPairs(popularCurrencyPairs: Currencies) {
        if (!popularCurrencyPairs?.currencies.isNullOrEmpty()) {
            val tempList = ArrayList<String>()
            popularCurrencyPairs?.currencies?.forEach { currencyPair ->
                currencyPair?.key?.let {
                    tempList.add(currencyPair.key!!)
                }
            }
            _popularCurrencyPairs.value = tempList
        } else {
            _showError.value = app.getString(R.string.no_popular_currency_pairs)
        }
    }

    fun clearCurrencyPairs() {
        _currencyPairs.value?.let {
            val currencyPairs = it as ArrayList<String>
            currencyPairs.clear()
            _currencyPairs.value = it
        }
    }

    fun setCurrencyPair(frmIndx: Int, ToIndx: Int) {
        _currencyPair.value =
            "${availableCurrencies.value?.get(frmIndx) ?: ""}${availableCurrencies.value?.get(ToIndx)}"
    }

    fun addCreatedPairToList() {
        _currencyPair.value?.let { addCurrencyPairToList(it) }
    }

    fun addPopularPairToList(indx: Int) {
        val popularCurrencyPairs = _popularCurrencyPairs.value as ArrayList<String>
        addCurrencyPairToList(popularCurrencyPairs[indx])
    }

    fun addCurrencyPairToList(currencyPair: String) {
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.add(currencyPair)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} currency pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }

    fun deleteCurrencyPairFromList(indx: Int) {
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.removeAt(indx)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }

    fun deleteTradeHistoryFromList(indx: Int){
        _pairTradeHistories.value?.let { pairHistories ->
            val currencyPairs = pairHistories as ArrayList
            currencyPairs.removeAt(indx)
        }
    }

/*
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
            _pairTradeHistories.value = getPairHistoryList(startDate, endDate, _currencyPairs.value!!, series?.price!!)
            cacheHistory()
        }
        else{
            _showError.value = app.getString(R.string.no_data_found)
        }
    }


    fun cacheHistory(){
        ioScope.launch {
            _pairTradeHistories.value?.let { fXRepository.addAllPairTradeHistoriesToDb(it) }
        }
    }
*/

    val catImagesFlow = Pager(config = PagingConfig(pageSize = 3)) {
        PairPagingSource(_startDate.value ?: "", _endDate.value ?: "", getCurrencyPairsString(), fXRepository)
    }.flow.cachedIn(viewModelScope)

    val viewState = MutableLiveData<HistoryState>()

    fun onViewEvent(historyEvent: HistoryEvent) = when (historyEvent) {
        is HistoryEvent.Refresh -> refresh()
        is HistoryEvent.pairHistoryChanged -> onPairHistoryChanged(
            position = historyEvent.position,
            pairHistory = historyEvent.pairHistory
        )
    }

    private fun refresh() {
        viewState.value = HistoryState.Load()
    }

    private fun onPairHistoryChanged(position: Int, pairHistory: PairHistoryTable) {
        val dfdf = pairHistory
    }

    fun getCurrencyPairsString(): String{
        var currency = ""
        _currencyPairs.value?.let {
            for((index, pair) in it.withIndex()) {
                currency += if (index > 0) ",$pair" else "$pair"
            }
        }
        return currency
    }

}



