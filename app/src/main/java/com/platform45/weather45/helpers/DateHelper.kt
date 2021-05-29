package com.platform45.weather45.helpers

import java.text.SimpleDateFormat
import java.util.*


fun getCurrentDate():String{
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

fun getDaysAgo(daysAgo: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(calendar.time)
}
