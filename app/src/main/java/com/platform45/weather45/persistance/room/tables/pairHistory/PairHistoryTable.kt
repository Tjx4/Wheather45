package com.platform45.weather45.persistance.room.tables.pairHistory

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pairHistories")
data class PairHistoryTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long = 0L,
    @ColumnInfo(name ="start_date")
    var startDate: String?,
    @ColumnInfo(name = "end_date")
    var endDate: String?,
    @ColumnInfo(name ="price")
    var price: String?,
): Parcelable