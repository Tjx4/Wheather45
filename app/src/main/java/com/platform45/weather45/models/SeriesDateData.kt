package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class SeriesDateData(
    @SerializedName("close") var close: Double?,
    @SerializedName("high") var high: Double?,
    @SerializedName("low") var low: Double?,
    @SerializedName("open") var open: Double?
)
