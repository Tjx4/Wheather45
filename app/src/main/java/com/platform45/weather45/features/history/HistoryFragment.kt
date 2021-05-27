package com.platform45.weather45.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.platform45.weather45.R
import com.platform45.weather45.adapters.FxAdapter
import com.platform45.weather45.adapters.PairAdapter
import com.platform45.weather45.base.fragments.BaseFragment
import com.platform45.weather45.databinding.FragmentHistoryBinding
import com.platform45.weather45.extensions.pixelToDp
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryFragment : BaseFragment(), PairAdapter.AddPairClickListener {
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

    private fun addObservers() {
        historyViewModel.pairs.observe(this, Observer { onTradingPairsSet(it) })
        historyViewModel.pairTradeHistories.observe(this, Observer { onTradeHistorySet(it) })
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
        val tradesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //tradesLayoutManager.initialPrefetchItemCount = tradeHistories?.size ?: 0
        rvtrades?.layoutManager = tradesLayoutManager

        val fxtAdapter = FxAdapter(requireContext(), tradeHistories)
        rvtrades?.adapter = fxtAdapter
        avlLoader.visibility = View.GONE
    }

    override fun onPairClicked(view: View, position: Int) {
        Toast.makeText(activity, "$position", Toast.LENGTH_SHORT).show()
    }
    fun goToConvertion(){
        // findNavController().navigate(R.id.history_to_conversion)
    }


}