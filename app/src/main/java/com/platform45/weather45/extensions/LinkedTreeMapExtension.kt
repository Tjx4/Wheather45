package com.platform45.weather45.extensions

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

fun LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?.mapToString(): String {
    return if(this == null) "" else Gson().toJson(this)
}

