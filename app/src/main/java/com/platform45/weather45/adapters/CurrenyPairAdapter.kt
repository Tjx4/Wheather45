package com.platform45.weather45.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.platform45.weather45.R


class CurrenyPairAdapter(private val context: Context, private val addSlides: List<String?>?) : RecyclerView.Adapter<CurrenyPairAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var addPairClickListener: AddPairClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.pair_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPair = addSlides?.get(position)
        holder.conversionTv.text = currentPair
        holder.imbDeleteImgb.setOnClickListener {
            ((addSlides) as ArrayList).remove(currentPair)
            notifyDataSetChanged()
            Toast.makeText(context, "$currentPair deleted", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var conversionTv = itemView.findViewById<TextView>(R.id.tvPair)
        internal var imbDeleteImgb = itemView.findViewById<ImageButton>(R.id.btnAddCurrencyPair)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            addPairClickListener?.onPairClicked(view, adapterPosition)
        }
    }

    interface AddPairClickListener {
        fun onPairClicked(view: View, position: Int)
    }

    fun setPairClickListener(addPairClickListener: AddPairClickListener) {
        this.addPairClickListener = addPairClickListener
    }

    override fun getItemCount() = addSlides?.size ?: 0

}
