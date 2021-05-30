package com.platform45.weather45.persistance.room.tables.pairHistory

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PairHistoryDAO {
    @Insert
    fun insert(pairHistoryTable: PairHistoryTable)

    @Update
    fun update(pairHistoryTable: PairHistoryTable)

    @Delete
    fun delete(pairHistoryTable: PairHistoryTable)

    @Query("SELECT * FROM pairHistories WHERE id = :key")
    fun get(key: Long): PairHistoryTable?

    @Query("SELECT * FROM pairHistories ORDER BY id DESC LIMIT 1")
    fun getLastUser(): PairHistoryTable?

    @Query("SELECT * FROM pairHistories ORDER BY id DESC")
    fun getAllUsersLiveData(): LiveData<List<PairHistoryTable>>

    @Query("SELECT * FROM pairHistories ORDER BY id DESC")
    fun getAllUsers():List<PairHistoryTable>?

    @Query("DELETE  FROM pairHistories")
    fun clear()
}