package com.platform45.weather45.features.fx

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.platform45.weather45.R
import com.platform45.weather45.base.activities.BaseActivity
import com.platform45.weather45.databinding.ActivityFxBinding
import com.platform45.weather45.models.PairTradeHistory
import kotlinx.android.synthetic.main.activity_fx.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class FxActivity : BaseActivity() {
    //https://www.youtube.com/watch?v=DD1CxoVONFE
    //AnyChart
    private lateinit var binding: ActivityFxBinding
    private val fxViewModel: FxViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fx)
        binding.fxViewModel = fxViewModel
        binding.lifecycleOwner = this
        addObservers()

        candleStickChart.isHighlightPerDragEnabled = true

        candleStickChart.setDrawBorders(true)

        candleStickChart.setBorderColor(resources.getColor(R.color.greyText))

        val yAxis = candleStickChart.axisLeft
        val rightAxis = candleStickChart.axisRight
        yAxis.setDrawGridLines(false)
        rightAxis.setDrawGridLines(false)
        candleStickChart.requestDisallowInterceptTouchEvent(true)

        val xAxis = candleStickChart.xAxis
        xAxis.setDrawGridLines(false) // disable x axis grid lines
        xAxis.setDrawLabels(false)
        rightAxis.textColor = Color.BLACK
        yAxis.setDrawLabels(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setAvoidFirstLastClipping(true)

        val l = candleStickChart.legend
        l.isEnabled = false
    }

    private fun addObservers() {
        fxViewModel.tradingPair.observe(this, Observer { onTradingPairSet(it) })
        fxViewModel.pairTrades.observe(this, Observer { onTradeHistorySet(it) })
    }

    private fun onTradingPairSet(tradingPair: String?){
        //Fetch location weather
    }

    fun onTradeHistorySet(allDayData: List<PairTradeHistory?>?){

        val dayData = allDayData?.get(0)?.history

        val xValues = ArrayList<String>()
        val candleEntries = ArrayList<CandleEntry>()
        dayData?.let {
            for ((indx, seriesCurrent) in dayData.withIndex()){
                val high = seriesCurrent?.high ?: 0f
                val low = seriesCurrent?.low ?: 0f
                val open = seriesCurrent?.open ?: 0f
                val close = seriesCurrent?.close ?: 0f
                candleEntries.add(CandleEntry(indx.toFloat(), high, low, open, close))
                xValues.add(seriesCurrent?.dateTime ?: "")
            }
        }

        val candleDataSet = CandleDataSet(candleEntries, "Entries")
        candleDataSet.color = Color.BLUE
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = Color.RED
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.rgb(122, 242, 84)
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.neutralColor = Color.BLUE
        candleDataSet.valueTextColor = Color.RED
        candleDataSet.setDrawValues(true)


        val xAxis = candleStickChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter { value, axis ->
            xValues[0]
        }
        //xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.labelRotationAngle = -90f
        xAxis.setLabelCount(xValues.count(),  false)
        //candleStickChart.extraBottomOffset = 160f


        val candleData = CandleData(candleDataSet)
        candleStickChart.setDrawGridBackground(false)
        candleStickChart.axisRight.isEnabled = true
        candleStickChart.description.isEnabled = false
        candleStickChart.data = candleData
        //candleStickChart.animateXY(5000, 4000)
        candleStickChart.invalidate()
    }


}