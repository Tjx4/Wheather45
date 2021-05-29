package com.platform45.weather45.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.platform45.weather45.R
import com.platform45.weather45.adapters.FxAdapter
import com.platform45.weather45.adapters.CurrencyPairAdapter
import com.platform45.weather45.base.fragments.BaseFragment
import com.platform45.weather45.customViews.MySpinner
import com.platform45.weather45.databinding.FragmentHistoryBinding
import com.platform45.weather45.extensions.getScreenCols
import com.platform45.weather45.features.history.datetime.DateTimePickerFragment
import com.platform45.weather45.helpers.showDateTimeDialogFragment
import com.platform45.weather45.helpers.showErrorDialog
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment(), CurrencyPairAdapter.AddPairClickListener, DateTimePickerFragment.DateTimeSetter{
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvtrades)
    }

    private fun addObservers() {
        historyViewModel.showLoading.observe(this, Observer { onShowLoading(it)})
        historyViewModel.showError.observe(this, Observer { onShowError(it)})
        historyViewModel.canProceed.observe(this, Observer { canProceed(it)})
        historyViewModel.popularCurrencyPairs.observe(this, Observer { onPopularCurrencyPairsSet(it)})
        historyViewModel.currencyPairs.observe(this, Observer { onCurrencyPairsSet(it)})
        historyViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it)})
        historyViewModel.isPairsUpdated.observe(this, Observer { onPairsListUpdated(it)})
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spnPorpularTradingPairs.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        spnToCurrency.setSelection(2)
        spnToCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                historyViewModel.setCurrencyPair(spnFrmCurrency.selectedItemPosition, position)
            }
        }

        btnFrom.setOnClickListener {
            indx = 0
            showDateTimeDialogFragment(this, (it as Button).text.toString())
        }

        btnTo.setOnClickListener {
            indx = 1
            showDateTimeDialogFragment(this, (it as Button).text.toString())
        }

        btnAddCurrencyPair.setOnClickListener {
            historyViewModel.addCreatedPairToList()
        }

        btnGetHistory.setOnClickListener {
            historyViewModel.showLoadingAndGetPairSeries()
        }
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        when (indx) {
            0 -> btnFrom.text = "$year-$month-$day" //Todo fix
            1 -> btnTo.text = "$year-$month-$day"
        }
    }

    override fun setTime(scheduledTime: String) {
        when (indx) {
            0 -> btnFrom.text = "${btnFrom.text}-$scheduledTime"
            1 -> btnTo.text = "${btnTo.text}-$scheduledTime"
        }
    }

    private fun onShowLoading(showLoading: Boolean){
        flLoader.visibility = View.VISIBLE
    }

    fun onShowError(erromMessage: String){
        flLoader.visibility = View.GONE
        showErrorDialog(requireContext(), "Error", erromMessage, "Close")
    }

    fun showPairSelector(){
        flLoader.visibility = View.GONE
        clPairSelector.visibility = View.VISIBLE
        clPairSeriesInfo.visibility = View.GONE
        myDrawerController.showSelectionMode()
    }

    fun showPairSeriesInfo() {
        flLoader.visibility = View.GONE
        clPairSelector.visibility = View.GONE
        clPairSeriesInfo.visibility = View.VISIBLE
        myDrawerController.showContent()
    }

    private fun onPopularCurrencyPairsSet(currency: List<String?>){
        showPairSelector()
    }

    private fun canProceed(proceed: Boolean){
        btnGetHistory.isEnabled = proceed
        btnGetHistory.background = resources.getDrawable( if(proceed) R.drawable.fx_button_background  else R.drawable.fx_disabled_button_background)
        tvRequestingPairs.visibility = if(proceed) View.VISIBLE else View.GONE
//vDivider.visibility = View.VISIBLE
    }

    private fun onCurrencyPairsSet(pairs: List<String>) {
        val pairsAdapter = CurrencyPairAdapter(requireContext(), historyViewModel, pairs)
        pairsAdapter.setPairClickListener(this)

        val cols = requireActivity().getScreenCols(125f)

        val requestingPairsManager = GridLayoutManager(context, cols)
        rvRequestingPairs?.adapter = pairsAdapter
        rvRequestingPairs?.layoutManager = requestingPairsManager

        val pairsManager = GridLayoutManager(context, cols)
        rvPairs?.adapter = pairsAdapter
        rvPairs?.layoutManager = pairsManager
    }

    fun onPairsListUpdated(isUpdated: Boolean){
        rvRequestingPairs?.adapter?.notifyDataSetChanged()
        rvPairs?.adapter?.notifyDataSetChanged()
        rvtrades?.adapter?.notifyDataSetChanged()
    }

    fun onTradeHistorySet(tradeHistories: List<PairTradeHistory?>?){
        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvtrades?.layoutManager = tradesLayoutManager
        val fxtAdapter = FxAdapter(requireContext(), tradeHistories)
        rvtrades?.adapter = fxtAdapter
        showPairSeriesInfo()
    }

    override fun onPairClicked(view: View, position: Int) {
        //rvPairs.layoutManager?.scrollToPosition(position)
    }


}