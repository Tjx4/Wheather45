package com.platform45.weather45.base.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.platform45.weather45.constants.ACTIVITY_TRANSITION
import com.platform45.weather45.constants.PAYLOAD_KEY

abstract class BaseActivity : AppCompatActivity() {
    protected var isNewActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTransitions(this)
        isNewActivity = true
    }

    private fun initTransitions(activity: Activity) {
        try {
            val activityTransition = activity.intent.getBundleExtra(PAYLOAD_KEY)?.getIntArray(ACTIVITY_TRANSITION)
            activity.overridePendingTransition(activityTransition!![0], activityTransition[1])
        }
        catch (e: Exception) {
            Log.e("AT", "$e")
        }
    }


}