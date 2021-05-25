package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") var id: Int?,
    @SerializedName("name") var name: String?,
    @SerializedName("country") var country: String?,
)
