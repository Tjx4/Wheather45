package com.platform45.weather45.extensions

import com.platform45.weather45.models.Series
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable

fun Series.toPairHistoryTable() = PairHistoryTable(`startDate` = this.startDate, `endDate` = this.endDate, `price` = this.price.mapToString())
fun PairHistoryTable.toSeries() = Series(this.startDate, this.endDate, this.price?.toLinkedTreeMap(), null)


