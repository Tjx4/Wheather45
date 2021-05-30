package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class Series(
    @SerializedName("start_date") var startDate: String?,
    @SerializedName("end_date") var endDate: String?,
    @SerializedName("price") var price: Object?,
    @SerializedName("error") var error: ResponseError?
)
