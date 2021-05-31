package com.platform45.weather45.features.convertion

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.platform45.weather45.R
import com.platform45.weather45.base.fragments.BaseFragment
import com.platform45.weather45.databinding.FragmentConversionBinding
import com.platform45.weather45.models.Conversion
import kotlinx.android.synthetic.main.fragment_conversion.*
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversionFragment : BaseFragment() {
    private lateinit var binding: FragmentConversionBinding
    private val conversionViewModel: ConversionViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setTitle(getString(R.string.convert_currencies))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myDrawerController.hideMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversion, container, false)
        binding.lifecycleOwner = this
        binding.conversionViewModel = conversionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.convert_currencies)

        addObservers()

        btnConvert.setOnClickListener {
            conversionViewModel.checkAndConvert()
        }
    }

    private fun addObservers() {
        conversionViewModel.convert.observe(this, Observer { onConversion(it) })
        conversionViewModel.showLoading.observe(this, Observer { onShowLoading(it)})
    }

    private fun onConversion(conversion: Conversion?){
        cnvLoader.visibility = View.GONE
        tvTotal.visibility = View.VISIBLE
    }

    private fun onShowLoading(showLoading: Boolean){
        cnvLoader.visibility = View.VISIBLE
    }
}