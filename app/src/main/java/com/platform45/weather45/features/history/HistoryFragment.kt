package com.platform45.weather45.features.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.platform45.weather45.R
import com.platform45.weather45.adapters.FxAdapter
import com.platform45.weather45.databinding.FragmentHistoryBinding
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.activity_fx.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), FxAdapter.AddTradeClickListener {
    private lateinit var binding: FragmentHistoryBinding
    private val fxViewModel: FxViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = this
        binding.fxViewModel = fxViewModel
        addObservers()
        return binding.root
    }

    private fun addObservers() {
        fxViewModel.tradingPair.observe(this, Observer { onTradingPairSet(it) })
        fxViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it) })
    }


    private fun onTradingPairSet(tradingPair: String?){

    }

    fun onTradeHistorySet(tradeHistories: List<PairTradeHistory?>?){
        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tradesLayoutManager.initialPrefetchItemCount = tradeHistories?.size ?: 0
        rvtrades?.layoutManager = tradesLayoutManager

        val fxtAdapter = FxAdapter(requireContext(), tradeHistories)
        fxtAdapter.setTradeClickListener(this)
        rvtrades?.adapter = fxtAdapter
        avlLoader.visibility = View.GONE
    }

    override fun onTradeClicked(view: View, position: Int) {

    }

}