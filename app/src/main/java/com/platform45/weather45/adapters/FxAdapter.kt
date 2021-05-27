package com.platform45.weather45.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.platform45.weather45.R
import com.platform45.weather45.models.PairTradeHistory


class FxAdapter(private val context: Context, private val addSlides: List<PairTradeHistory?>?) : RecyclerView.Adapter<FxAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.fx_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFx = addSlides?.get(position)

        holder.conversionTv.text = currentFx?.tradingPair
        holder.timePeriodTv.text = "From: ${currentFx?.startDate} to ${currentFx?.endDate}"

        val dayData = currentFx?.history

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

        val xAxis = holder.candleStickChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.labelRotationAngle = -90f
        xAxis.setLabelCount(xValues.count(),  false)
        //candleStickChart.extraBottomOffset = 160f


        val candleData = CandleData(candleDataSet)
        holder.candleStickChart.setDrawGridBackground(false)
        holder.candleStickChart.axisLeft.isEnabled = false
        holder.candleStickChart.axisRight.isEnabled = true
        holder.candleStickChart.description.isEnabled = false
        holder.candleStickChart.data = candleData
        //candleStickChart.animateXY(5000, 4000)
        holder.candleStickChart.invalidate()

    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var conversionTv = itemView.findViewById<TextView>(R.id.tvConversion)
        internal var timePeriodTv = itemView.findViewById<TextView>(R.id.tvTimePeriod)
        internal var candleStickChart = itemView.findViewById<CandleStickChart>(R.id.candleStickChart)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
        }
    }

    override fun getItemCount() = addSlides?.size ?: 0

}
