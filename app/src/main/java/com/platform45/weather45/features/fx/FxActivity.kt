package com.platform45.weather45.features.fx

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.platform45.weather45.R
import com.platform45.weather45.adapters.FxAdapter
import com.platform45.weather45.base.activities.BaseActivity
import com.platform45.weather45.databinding.ActivityFxBinding
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.activity_fx.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class FxActivity : BaseActivity(), FxAdapter.AddTradeClickListener {

    private lateinit var binding: ActivityFxBinding
    private val fxViewModel: FxViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fx)
        binding.fxViewModel = fxViewModel
        binding.lifecycleOwner = this
        addObservers()
    }

    private fun addObservers() {
        fxViewModel.tradingPair.observe(this, Observer { onTradingPairSet(it) })
        fxViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it) })
    }

    private fun onTradingPairSet(tradingPair: String?){

    }

    fun onTradeHistorySet(tradeHistories: List<PairTradeHistory?>?){
        val tradesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tradesLayoutManager.initialPrefetchItemCount = tradeHistories?.size ?: 0
        rvtrades?.layoutManager = tradesLayoutManager

        val fxtAdapter = FxAdapter(this, tradeHistories)
        fxtAdapter.setTradeClickListener(this)
        rvtrades?.adapter = fxtAdapter
        avlLoader.visibility = View.GONE
    }

    override fun onTradeClicked(view: View, position: Int) {

    }


}