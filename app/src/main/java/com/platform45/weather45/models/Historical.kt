package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class Historical(
    @SerializedName("date") var date: String?,
    @SerializedName("price") var price: Object?,
)
