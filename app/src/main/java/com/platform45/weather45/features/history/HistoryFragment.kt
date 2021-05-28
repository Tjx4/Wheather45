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
import com.platform45.weather45.databinding.FragmentHistoryBinding
import com.platform45.weather45.features.history.datetime.DateTimePickerFragment
import com.platform45.weather45.helpers.showDateTimeDialogFragment
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class HistoryFragment : BaseFragment(), PairAdapter.AddPairClickListener, DateTimePickerFragment.DateTimeSetter{
    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myDrawerController.showMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = this
        binding.fxViewModel = historyViewModel
        addObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            showDateTimeDialogFragment(this)
        }

        btnTo.setOnClickListener {
            showDateTimeDialogFragment(this)
        }

        btnGetHistory.setOnClickListener {

        }
    }

    override fun setDate(year: Int, month: Int, day: Int) {

    }

    override fun setTime(scheduledTime: String) {

    }


    private fun addObservers() {
        historyViewModel.currencies.observe(this, Observer { onCurrenciesSet(it) })
        historyViewModel.pairs.observe(this, Observer { onTradingPairsSet(it) })
        historyViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it) })
    }

    private fun onCurrenciesSet(currecies: List<String>) {

    }

    private fun onTradingPairsSet(pairs: List<String?>?){
        val getCols = { columnWidthDp: Float ->
            val displayMetrics = requireContext().resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            (screenWidthDp / columnWidthDp + 0.5).toInt()
        }

        val cols = getCols(120f)
        val pairsManager = GridLayoutManager(context, cols)
            pairsManager.initialPrefetchItemCount = pairs?.size ?: 0
            rvPairs?.layoutManager = pairsManager

            val pairsAdapter = PairAdapter(requireContext(), pairs)
            pairsAdapter.setPairClickListener(this)
            rvPairs?.adapter = pairsAdapter

    }

    fun onTradeHistorySet(tradeHistories: List<PairTradeHistory?>?){
        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //tradesLayoutManager.initialPrefetchItemCount = tradeHistories?.size ?: 0
        rvtrades?.layoutManager = tradesLayoutManager
        //rvtrades.layoutManager = ViewPagerLayoutManager(activity)

        val fxtAdapter = FxAdapter(requireContext(), tradeHistories)
        rvtrades?.adapter = fxtAdapter
        avlLoader.visibility = View.GONE

        val helper: SnapHelper = PagerSnapHelper()
        helper.attachToRecyclerView(rvtrades)
    }

    override fun onPairClicked(view: View, position: Int) {
        Toast.makeText(context, "$position clicked", Toast.LENGTH_SHORT).show()
        rvPairs.scrollToPosition(position)
    }
    fun goToConvertion(){
        // findNavController().navigate(R.id.history_to_conversion)
    }

}