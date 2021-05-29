package com.platform45.weather45.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.platform45.weather45.R
import com.platform45.weather45.constants.ACTIVITY_TRANSITION
import com.platform45.weather45.constants.PAYLOAD_KEY
import com.platform45.weather45.models.Transition

val SLIDE_IN_ACTIVITY = getTransitionAnimation(R.anim.slide_right, R.anim.no_transition)
val SLIDE_OUT_ACTIVITY =  getTransitionAnimation(R.anim.no_transition, R.anim.slide_left)
val FADE_IN_ACTIVITY = getTransitionAnimation(R.anim.fade_in, R.anim.no_transition)

fun AppCompatActivity.navigateToActivity(
    activity: Class<*>,
    payload: Bundle?,
    transitionAnimation: Transition
) {
    goToActivity(activity, transitionAnimation, payload)
}

private fun AppCompatActivity.goToActivity(activity: Class<*>, transitionAnimation: Transition, payload: Bundle?) {
    val intent = Intent(this, activity)

    val fullPayload = payload ?: Bundle()
    fullPayload.putIntArray(ACTIVITY_TRANSITION, intArrayOf(transitionAnimation.inAnimation, transitionAnimation.outAnimation))

    intent.putExtra(PAYLOAD_KEY, fullPayload)
    startActivity(intent)
}

private fun getTransitionAnimation(inAnimation: Int, outAnimation: Int): Transition {
    val transitionProvider = Transition()
    transitionProvider.inAnimation = inAnimation
    transitionProvider.outAnimation = outAnimation
    return transitionProvider
}

fun Activity.getScreenCols(columnWidthDp: Float): Int{
    val displayMetrics = this.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}