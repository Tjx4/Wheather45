package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class Series(
    @SerializedName("start_date") var startDate: String?,
    @SerializedName("end_date") var endDate: String?,
    @SerializedName("price") var price: LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?,
    @SerializedName("error") var error: ResponseError?
)
