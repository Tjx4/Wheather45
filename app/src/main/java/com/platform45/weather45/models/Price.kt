package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class Price (
    @SerializedName("price") var price: Price?,
    /*"price": {
    "EURUSD": 1.19509,
    "GBPUSD": 1.35748,
    "USDJPY": 109.8405*/
)
