package com.platform45.weather45.di

import com.platform45.weather45.networking.retrofit.API
import org.koin.dsl.module

val networkingModule = module {
    single { API.retrofit }
}