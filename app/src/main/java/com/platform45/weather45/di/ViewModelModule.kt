package com.platform45.weather45.di

import com.platform45.weather45.features.convertion.ConversionViewModel
import com.platform45.weather45.features.history.HistoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HistoryViewModel(androidApplication(), get()) }
    viewModel { ConversionViewModel(androidApplication(), get()) }
}
