package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("city") var city: City?,
    @SerializedName("list") var list: List<DailyWeatherDetails?>?
)