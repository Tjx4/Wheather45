package com.platform45.weather45.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.platform45.weather45.R
import com.platform45.weather45.helpers.toDayDataList
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable

class FxPagingAdapter(val context: Context) : PagingDataAdapter<PairHistoryTable, FxPagingAdapter.HistoryViewHolder>(HistoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fx_layout,
            parent,
            false
        )
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holderHistory: HistoryViewHolder, position: Int) {
        val currentFx = getItem(position)
        holderHistory.conversionTv.text = currentFx?.tradingPair
        holderHistory.timePeriodTv.text = "From: ${currentFx?.startDate} to ${currentFx?.endDate}"

        val dayData = currentFx?.history?.toDayDataList()

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
        candleDataSet.color = context.resources.getColor(R.color.blue_1)
        candleDataSet.shadowColor = context.resources.getColor(R.color.greyText)
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = context.resources.getColor(R.color.red_2)
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = context.resources.getColor(R.color.green_1)
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.neutralColor = Color.BLUE
        candleDataSet.valueTextColor = context.resources.getColor(R.color.lightText)
        candleDataSet.setDrawValues(true)

        val xAxis = holderHistory.candleStickChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.labelRotationAngle = -90f
        xAxis.setLabelCount(xValues.count(),  false)
        xAxis.textColor = context.resources.getColor(R.color.light_text_2)
        xAxis.axisMinimum = 10f
        //xAxis.spaceMax = 1f
        //candleStickChart.extraBottomOffset = 160f

        val aAxisRight = holderHistory.candleStickChart.axisRight
        aAxisRight.textColor = context.resources.getColor(R.color.light_text_2)

        val candleData = CandleData(candleDataSet)
        holderHistory.candleStickChart.setDrawGridBackground(false)
        holderHistory.candleStickChart.axisLeft.isEnabled = false
        holderHistory.candleStickChart.axisRight.isEnabled = true
        holderHistory.candleStickChart.description.isEnabled = false
        holderHistory.candleStickChart.data = candleData
        //candleStickChart.animateXY(5000, 4000)
        holderHistory.candleStickChart.invalidate()
    }

    inner class HistoryViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var conversionTv = itemView.findViewById<TextView>(R.id.tvConversion)
        internal var timePeriodTv = itemView.findViewById<TextView>(R.id.tvTimePeriod)
        internal var candleStickChart = itemView.findViewById<CandleStickChart>(R.id.candleStickChart)

        init {
           // itemView.setOnClickListener(this)
        }
    }
}

object HistoryComparator : DiffUtil.ItemCallback<PairHistoryTable>() {
    override fun areItemsTheSame(oldItem: PairHistoryTable, newItem: PairHistoryTable) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: PairHistoryTable, newItem: PairHistoryTable) = oldItem == newItem
}