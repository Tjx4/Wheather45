package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class Historical(
    @SerializedName("date") var date: String?,
    @SerializedName("price") var price: JvmType.Object?,
)
