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

class CurrencyPairAdapter(private val context: Context, private val addSlides: List<String>) : RecyclerView.Adapter<CurrencyPairAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var userInteractions: UserInteractions? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.pair_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPair = addSlides?.get(position)
        holder.conversionTv.text = currentPair
        holder.imbDeleteImgb.setOnClickListener {
            userInteractions?.onDeleteClicked(position)
        }
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var conversionTv = itemView.findViewById<TextView>(R.id.tvPair)
        internal var imbDeleteImgb = itemView.findViewById<ImageButton>(R.id.btnAddCurrencyPair)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            userInteractions?.onPairClicked(view, adapterPosition)
        }
    }

    interface UserInteractions {
        fun onPairClicked(view: View, position: Int)
        fun onDeleteClicked(position: Int)
    }

    fun setPairClickListener(userInteractions: UserInteractions) {
        this.userInteractions = userInteractions
    }

    override fun getItemCount() = addSlides?.size

}
