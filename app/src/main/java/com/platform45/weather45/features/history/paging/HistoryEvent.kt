package com.platform45.weather45.features.history.paging

import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable

sealed class HistoryEvent {
    object Refresh : HistoryEvent()

    data class pairHistoryChanged(
        val position: Int,
        val pairHistory: PairHistoryTable
    ) : HistoryEvent()
}