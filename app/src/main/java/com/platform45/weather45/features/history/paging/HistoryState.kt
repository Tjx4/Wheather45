package com.platform45.weather45.features.history.paging

sealed class HistoryState {
    data class Load(val position: Int? = null) : HistoryState()
    data class Error(val message: String? = null) : HistoryState()
}