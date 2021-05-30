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

    @ColumnInfo(name = "pairHistory")
    var pairHistory:String? = null

): Parcelable