package com.platform45.weather45.persistance.room.tables.pairHistory

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.platform45.weather45.models.DayData
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pairHistories")
data class PairHistoryTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long = 0L,
    @ColumnInfo(name ="tradingPair")
    var tradingPair: String?,
    @ColumnInfo(name ="startDate")
    var startDate: String?,
    @ColumnInfo(name ="endDate")
    var endDate: String?,
    @ColumnInfo(name ="history")
    var history: String?
): Parcelable