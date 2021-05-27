package com.platform45.weather45.features.convertion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platform45.weather45.R
import com.platform45.weather45.databinding.FragmentConversionBinding
import com.platform45.weather45.models.Conversion
import kotlinx.android.synthetic.main.fragment_conversion.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversionFragment : Fragment() {
    private lateinit var binding: FragmentConversionBinding
    private val conversionViewModel: ConversionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversion, container, false)
        binding.lifecycleOwner = this
        binding.conversionViewModel = conversionViewModel
        addObservers()
        return binding.root
    }

    private fun addObservers() {
        conversionViewModel.convert.observe(this, Observer { onConversion(it) })
    }

    private fun onConversion(conversion: Conversion?){
        tvTotal.visibility = View.VISIBLE
    }

    fun onConvertButtonClicked(view: View){
        conversionViewModel.checkAndConvert()
    }

}