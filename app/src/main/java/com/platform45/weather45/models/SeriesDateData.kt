package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class SeriesDateData(
    @SerializedName("close") var close: Float?,
    @SerializedName("high") var high: Float?,
    @SerializedName("low") var low: Float?,
    @SerializedName("open") var open: Float?
)


