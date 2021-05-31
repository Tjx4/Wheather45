package com.platform45.weather45.features.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.*
import com.platform45.weather45.R
import com.platform45.weather45.adapters.CurrencyPairAdapter
import com.platform45.weather45.adapters.FavouriteCPAdapter
import com.platform45.weather45.adapters.FxPagingAdapter
import com.platform45.weather45.base.fragments.BaseFragment
import com.platform45.weather45.customViews.MySpinner
import com.platform45.weather45.databinding.FragmentHistoryBinding
import com.platform45.weather45.extensions.getScreenCols
import com.platform45.weather45.features.history.datetime.DateTimePickerFragment
import com.platform45.weather45.helpers.showDateTimeDialogFragment
import com.platform45.weather45.helpers.showErrorDialog
import com.platform45.weather45.models.CurrencyPair
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment(), FavouriteCPAdapter.AddPairClickListener, CurrencyPairAdapter.UserInteractions, DateTimePickerFragment.DateTimeSetter{
    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModel()
    lateinit var fxPagingAdapter: FxPagingAdapter
    override var indx: Int = 0
    var isFirstTime = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setHistoryFragment(this)
        myDrawerController.setTitle(getString(R.string.app_name))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myDrawerController.showMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = this
        binding.fxViewModel = historyViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.app_name)

        addObservers()
        historyViewModel.checkState()

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
            initPaging()
        }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvtrades)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fxPagingAdapter = FxPagingAdapter(requireContext())

        rvtrades.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = fxPagingAdapter
        }

        fxPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading){
                flLoader.visibility = View.VISIBLE
            }
            else{
                flLoader.visibility = View.GONE

                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                // errorState?.let { Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show() }
            }
        }

    }

    private fun addObservers() {
        historyViewModel.showLoading.observe(this, Observer { onShowLoading(it)})
        //historyViewModel.loadRemote.observe(this, Observer { onLoadFromRemote(it)})
        historyViewModel.showError.observe(this, Observer { onShowError(it)})
        historyViewModel.canProceed.observe(this, Observer { canProceed(it)})
        historyViewModel.popularCurrencyPairs.observe(this, Observer { onPopularCurrencyPairsSet(it)})
        historyViewModel.currencyPairs.observe(this, Observer { onCurrencyPairsSet(it)})
        //historyViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it)})
        historyViewModel.isPairsUpdated.observe(this, Observer { onPairsListUpdated(it)})
    }

    fun initPaging(){
        lifecycleScope.launch {
            historyViewModel.pairHistoryFlow.collectLatest {
                fxPagingAdapter.submitData(it)
            }
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
        myDrawerController.hideToolbar()
        flLoader.visibility = View.VISIBLE
    }

    private fun onShowError(errorMessage: String){
        flLoader.visibility = View.GONE
        showErrorDialog(requireContext(), "Error", errorMessage, "Close")
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

fun resetPairData() {
    historyViewModel.clearCurrencyPairs()
}

    private fun onPopularCurrencyPairsSet(currencyPairs: List<CurrencyPair?>){
        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvPorpularCp?.layoutManager = tradesLayoutManager
        val favouriteCPAdapter = FavouriteCPAdapter(requireContext(), currencyPairs)
        favouriteCPAdapter.setPairClickListener(this)
        rvPorpularCp?.adapter = favouriteCPAdapter
        showPairSeriesInfo()
    }

    private fun canProceed(proceed: Boolean){
        btnGetHistory.isEnabled = proceed
        btnGetHistory.background = resources.getDrawable( if(proceed) R.drawable.fx_button_background  else R.drawable.fx_disabled_button_background)
        tvRequestingPairs.visibility = if(proceed) View.VISIBLE else View.GONE
    }

    private fun onCurrencyPairsSet(pairs: List<String>) {
        val pairsAdapter = CurrencyPairAdapter(requireContext(), pairs)
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

    /*
    fun onTradeHistorySet(pairHistories: List<PairTradeHistory?>?){

        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvtrades?.layoutManager = tradesLayoutManager
        val fxtAdapter = FxAdapter(requireContext(), pairHistories)
        rvtrades?.adapter = fxtAdapter
        showPairSeriesInfo()

    }
    */

    override fun onPairClicked(view: View, position: Int) {
        rvPairs.layoutManager?.scrollToPosition(position)
    }

    override fun onDeleteClicked(pair: String, position: Int) {
        historyViewModel.deleteCurrencyPairFromList(position)
        historyViewModel.deleteTradeHistoryFromList(position)
        Toast.makeText(context, "$pair deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onPairClicked(position: Int) {
        historyViewModel.addPopularPairToList(position)
        Toast.makeText(context, "${ historyViewModel.popularCurrencyPairs.value?.get(position)?.pair} clicked", Toast.LENGTH_SHORT).show()
    }

}