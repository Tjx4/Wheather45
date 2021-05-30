package com.platform45.weather45.persistance.room.tables.pairHistory

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PairHistoryDAO {
    @Insert
    fun insert(pairHistoryTable: PairHistoryTable)

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertAll(pairHistories: List<PairHistoryTable>)

    @Update
    fun update(pairHistoryTable: PairHistoryTable)

    @Delete
    fun delete(pairHistoryTable: PairHistoryTable)

    @Query("SELECT * FROM pairHistories WHERE id = :key")
    fun get(key: Long): PairHistoryTable?

    @Query("SELECT * FROM pairHistories ORDER BY id DESC LIMIT 1")
    fun getLastUser(): PairHistoryTable?

    @Query("SELECT * FROM pairHistories ORDER BY id DESC")
    fun getAllHistoriesLiveData(): LiveData<List<PairHistoryTable>>

    @Query("SELECT * FROM pairHistories ORDER BY id DESC")
    fun getAllHistories():List<PairHistoryTable>?

    @Query("DELETE  FROM pairHistories")
    fun clear()
}