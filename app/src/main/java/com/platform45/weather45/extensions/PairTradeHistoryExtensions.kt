package com.platform45.weather45.extensions

import com.platform45.weather45.helpers.mapToString
import com.platform45.weather45.helpers.toDayDataList
import com.platform45.weather45.models.PairTradeHistory
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable

fun PairTradeHistory.toDbTable() = PairHistoryTable(`tradingPair` = this.tradingPair , `startDate` = this.startDate, `endDate` = this.endDate, `history` = this.history.mapToString())
fun PairHistoryTable.toPairHistory() =  PairTradeHistory(this.tradingPair, this.startDate, this.endDate, this.history?.toDayDataList())


