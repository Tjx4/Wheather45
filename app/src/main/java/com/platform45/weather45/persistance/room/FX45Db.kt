package com.platform45.weather45.persistance.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryDAO
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable

@Database(entities = [PairHistoryTable::class], version = 1, exportSchema = false)
 abstract class FX45Db : RoomDatabase() {
    abstract val pairHistoryDAO: PairHistoryDAO

    companion object{
        @Volatile
        private var INSTANCE: FX45Db? = null

        fun getInstance(context: Context): FX45Db {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, FX45Db::class.java, "fx45_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return  instance
            }
        }
    }
}