package com.platform45.weather45

import android.app.Application
import com.platform45.weather45.di.ModuleLoadHelper
import com.platform45.weather45.di.networkingModule
import com.platform45.weather45.di.repositoryModule
import com.platform45.weather45.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    networkingModule
                ) + ModuleLoadHelper.getBuildSpecialModuleList()
            )
        }
    }


}