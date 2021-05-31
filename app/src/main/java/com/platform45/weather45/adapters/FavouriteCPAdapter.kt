package com.platform45.weather45.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.platform45.weather45.R
import com.platform45.weather45.models.CurrencyPair

class FavouriteCPAdapter(context: Context, private val currencyPairs: List<CurrencyPair?>?) : RecyclerView.Adapter<FavouriteCPAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var pairClickListener: AddPairClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.cp_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPair = currencyPairs?.get(position)
        holder.favCpTv.text = currentPair?.pair
        holder.favCpFnTv.text = currentPair?.name
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var favCpTv = itemView.findViewById<TextView>(R.id.tvFavCp)
        internal var favCpFnTv = itemView.findViewById<TextView>(R.id.tvFavCpFn)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            pairClickListener?.onPairClicked(adapterPosition)
        }
    }

    interface AddPairClickListener {
        fun onPairClicked(position: Int)
    }

    fun setPairClickListener(pairClickListener: AddPairClickListener) {
        this.pairClickListener = pairClickListener
    }

    override fun getItemCount() = currencyPairs?.size ?: 0

}