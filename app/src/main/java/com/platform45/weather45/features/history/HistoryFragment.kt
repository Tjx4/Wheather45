package com.platform45.weather45.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.platform45.weather45.R
import com.platform45.weather45.adapters.FxAdapter
import com.platform45.weather45.adapters.PairAdapter
import com.platform45.weather45.base.fragments.BaseFragment
import com.platform45.weather45.customViews.MySpinner
import com.platform45.weather45.databinding.FragmentHistoryBinding
import com.platform45.weather45.features.history.datetime.DateTimePickerFragment
import com.platform45.weather45.helpers.showDateTimeDialogFragment
import com.platform45.weather45.helpers.showErrorDialog
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment(), PairAdapter.AddPairClickListener, DateTimePickerFragment.DateTimeSetter{
    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModel()
    override var indx: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myDrawerController.badFrag(this)
        myDrawerController.showMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = this
        binding.fxViewModel = historyViewModel
        addObservers()
        return binding.root
    }

    private fun addObservers() {
        historyViewModel.showLoading.observe(this, Observer { onShowLoading(it) })
        historyViewModel.showError.observe(this, Observer { onShowError(it) })
        historyViewModel.popularCurrencyPairs.observe(this, Observer { onPopularCurrencyPairsSet(it) })
        historyViewModel.currency.observe(this, Observer { onCurrencyListUpdated(it) })
        historyViewModel.currencyPairs.observe(this, Observer { onCurrenciesSet(it) })
        historyViewModel.requestedPairs.observe(this, Observer { onRequestedPairsSet(it) })
        historyViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spnPorpularTradingPairs.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if((parent as MySpinner).isInit){
                    historyViewModel.addPopularPairToList(position)
                }
                else{
                    parent.isInit = true
                }
            }
        }

        spnFrmCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                historyViewModel.setCurrencyPair(position, spnToCurrency.selectedItemPosition)
            }
        }

        spnToCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                historyViewModel.setCurrencyPair(spnFrmCurrency.selectedItemPosition, position)
            }
        }

        btnFrom.setOnClickListener {
            indx = 0
            showDateTimeDialogFragment(this)
        }

        btnTo.setOnClickListener {
            indx = 1
            showDateTimeDialogFragment(this)
        }

        btnAddCurrencyPair.setOnClickListener {
            historyViewModel.addCurrentPairTolist()
        }

        btnGetHistory.setOnClickListener {
            historyViewModel.showLoadingAndGetPairSeries()
        }
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        when (indx) {
            0 -> {
                btnFrom.text = "$year-$month-$day"
            }
            1 -> {
                btnTo.text = "$year-$month-$day"
            }
        }
    }

    override fun setTime(scheduledTime: String) {
        when (indx) {
            0 -> {
                btnFrom.text = "${btnFrom.text}-$scheduledTime"
            }
            1 -> {

                btnTo.text = "${btnTo.text}-$scheduledTime"
            }
        }
    }

    private fun onShowLoading(showLoading: Boolean){
        flLoader.visibility = View.VISIBLE
        //clSearch.visibility = View.GONE
        //clContent.visibility = View.GONE
    }

    private fun onShowError(erromMessage: String){
        flLoader.visibility = View.GONE
        showErrorDialog(requireContext(), "Error", erromMessage, "Close")
    }

     fun toggleSelector(){
        flLoader.visibility = View.GONE
        clSearch.visibility = View.VISIBLE
        clContent.visibility = View.GONE
    }

    private fun onPopularCurrencyPairsSet(currency: List<String?>){
        flLoader.visibility = View.GONE
        clSearch.visibility = View.VISIBLE
        clContent.visibility = View.GONE
    }

    private fun onCurrencyListUpdated(currency: String){
        val pairs = currency.split(",")
        val cols = getCols(120f)
        val pairsManager = GridLayoutManager(context, cols)
        pairsManager.initialPrefetchItemCount = pairs?.size

        val pairsAdapter = PairAdapter(requireContext(), pairs)
        pairsAdapter.setPairClickListener(this)

        rvRequestingPairs?.adapter = pairsAdapter
        rvRequestingPairs?.layoutManager = pairsManager
        tvRequestingPairs.visibility = View.VISIBLE
        btnGetHistory.isEnabled = true
    }

    private fun onCurrenciesSet(currecies: List<String>) {

    }

    //Todo move
    fun getCols(columnWidthDp: Float): Int{
        val displayMetrics = requireContext().resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
       return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private fun onRequestedPairsSet(pairs: List<String?>?){
        val cols = getCols(120f)
        val pairsManager = GridLayoutManager(context, cols)
        pairsManager.initialPrefetchItemCount = pairs?.size ?: 0

        val pairsAdapter = PairAdapter(requireContext(), pairs)
        pairsAdapter.setPairClickListener(this)

        rvPairs?.adapter = pairsAdapter
        rvPairs?.layoutManager = pairsManager
    }

    fun onTradeHistorySet(tradeHistories: List<PairTradeHistory?>?){
        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //tradesLayoutManager.initialPrefetchItemCount = tradeHistories?.size ?: 0
        rvtrades?.layoutManager = tradesLayoutManager

        val fxtAdapter = FxAdapter(requireContext(), tradeHistories)
        rvtrades?.adapter = fxtAdapter

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(rvtrades)

        flLoader.visibility = View.GONE
        clSearch.visibility = View.GONE
        clContent.visibility = View.VISIBLE
    }

    override fun onPairClicked(view: View, position: Int) {
        Toast.makeText(context, "$position clicked", Toast.LENGTH_SHORT).show()
        rvPairs.scrollToPosition(position)
    }

}