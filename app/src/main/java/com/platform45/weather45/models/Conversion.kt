package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class Conversion(
    @SerializedName("price") var price: Double,
    @SerializedName("timestamp") var timestamp: Int?,
    @SerializedName("total") var total: Double?,
    @SerializedName("from") var from: String?,
    @SerializedName("to") var to: String?
)