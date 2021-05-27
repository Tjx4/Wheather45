package com.platform45.weather45.models

data class PairTradeHistory (
    var tradingPair: String?,
    var startDate: String?,
    var endDate: String?,
    var history: List<DayData?>?
)
