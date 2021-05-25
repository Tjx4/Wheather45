package com.platform45.weather45.features.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.platform45.weather45.extensions.FADE_IN_ACTIVITY
import com.platform45.weather45.extensions.navigateToActivity
import com.platform45.weather45.features.dailyTemperatures.DailyTemperaturesActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToActivity(DailyTemperaturesActivity::class.java, null, FADE_IN_ACTIVITY)
    }
}