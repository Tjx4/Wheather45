package com.platform45.weather45.extensions

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.platform45.weather45.models.DayData

fun String.toLinkedTreeMap(): LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>? {
    return Gson().fromJson(this,  object : TypeToken<LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?>() {}.type)
}

fun String.toDayDataList(): List<DayData?>? {
    return Gson().fromJson(this,  object : TypeToken<List<DayData?>?>() {}.type)
}

fun LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?.mapToString(): String {
    return if(this == null) "" else Gson().toJson(this)
}

fun List<DayData?>?.mapToString(): String {
    return if(this == null) "" else Gson().toJson(this)
}


