package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code") var code: String?,
    @SerializedName("info") var info: String?,
)
