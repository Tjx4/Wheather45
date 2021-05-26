package com.platform45.weather45.features.fx

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.platform45.weather45.R
import com.platform45.weather45.base.activities.BaseActivity
import com.platform45.weather45.databinding.ActivityFxBinding
import com.platform45.weather45.models.Conversion
import com.platform45.weather45.models.SeriesDateData
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

    fun onDataSet(seriesDateData: List<SeriesDateData?>?){
/*


        val set1 = CandleDataSet(yValsCandleStick, "DataSet 1")
        set1.color = Color.rgb(80, 80, 80)
        set1.shadowColor = resources.getColor(R.color.black)
        set1.shadowWidth = 0.8f
        set1.decreasingColor = resources.getColor(R.color.red)
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = resources.getColor(R.color.teal_700)
        set1.increasingPaintStyle = Paint.Style.FILL
        set1.neutralColor = Color.LTGRAY
        set1.setDrawValues(false)

        val data = CandleData(set1)
        candleStickChart.data = data
        candleStickChart.invalidate()
*/
/*
        val ceList: MutableList<CandleEntry> = ArrayList()
        ceList.add(CandleEntry(0f, 4.62f, 2.02f, 2.70f, 4.13f))
        ceList.add(CandleEntry(1f, 5.50f, 2.70f, 3.35f, 4.96f))
        ceList.add(CandleEntry(2f, 5.25f, 3.02f, 3.50f, 4.50f))
        ceList.add(CandleEntry(3f, 6f, 3.25f, 4.40f, 5.0f))
        ceList.add(CandleEntry(4f, 5.57f, 2f, 2.80f, 4.5f))
*/
        val ceList: MutableList<CandleEntry> = ArrayList()
        seriesDateData?.let {
            for ((indx, seriesCurrent) in seriesDateData.withIndex()){
                val high = seriesCurrent?.high ?: 0f
                val low = seriesCurrent?.low ?: 0f
                val open = seriesCurrent?.open ?: 0f
                val close = seriesCurrent?.close ?: 0f
                ceList.add(CandleEntry(indx.toFloat(), high, low, open, close))
            }
        }

        val cds = CandleDataSet(ceList, "Entries")
        cds.color = Color.rgb(80, 80, 80)
        cds.shadowColor = Color.DKGRAY
        cds.shadowWidth = 0.7f
        cds.decreasingColor = Color.RED
        cds.decreasingPaintStyle = Paint.Style.FILL
        cds.increasingColor = Color.rgb(122, 242, 84)
        cds.increasingPaintStyle = Paint.Style.FILL
        cds.neutralColor = Color.BLUE
        cds.valueTextColor = Color.RED
        val cd = CandleData(cds)
        candleStickChart.data = cd
        candleStickChart.invalidate()
    }


    private fun addObservers() {
        fxViewModel.conversion.observe(this, Observer { onConversion(it) })
        fxViewModel.seriesDateData.observe(this, Observer { onDataSet(it) })
    }

    private fun onConversion(conversion: Conversion?){
        //Fetch location weather
    }

}