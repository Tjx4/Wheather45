package com.platform45.weather45.extensions

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken

fun String.toLinkedTreeMap(): LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>? {
    return Gson().fromJson(this,  object : TypeToken<LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?>() {}.type)
}
