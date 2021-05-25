package com.platform45.weather45.di

import com.platform45.weather45.features.dailyTemperatures.DailyTemperaturesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DailyTemperaturesViewModel(androidApplication(), get()) }
}
