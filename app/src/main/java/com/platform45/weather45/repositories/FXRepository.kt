package com.platform45.weather45.repositories

import com.platform45.weather45.extensions.toDbTable
import com.platform45.weather45.extensions.toPairHistory
import com.platform45.weather45.models.*
import com.platform45.weather45.networking.retrofit.RetrofitHelper
import com.platform45.weather45.persistance.room.FX45Db
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable

class FXRepository(private val retrofitHelper: RetrofitHelper, private val database: FX45Db) {

    suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies?{
        return try {
            retrofitHelper.currencies(apiKey)
        }
        catch (ex: Exception) {
            null
        }
    }

    suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Series?{
        return try {
            retrofitHelper.series(apiKey, startDate, endDate, currency, format)
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getAllPairHistoriesFromDb() : List<PairTradeHistory?>??{
        return try {
            val tradeHistories = ArrayList<PairTradeHistory>()
            database.pairHistoryDAO.getAllHistories()?.forEach { tradeHistories.add(it.toPairHistory()) }
            tradeHistories
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getPairTradeHistoryFromDb(id: Long) : PairTradeHistory?{
        return try {
            database.pairHistoryDAO.get(id)?.toPairHistory()
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun addPairTradeHistoryToDb(pairTradeHistory: PairTradeHistory) : DbOperation {
        return try {
            database.pairHistoryDAO.insert(pairTradeHistory.toDbTable())
            DbOperation(true)
        }
        catch (ex: Exception){
            DbOperation(false, "$ex")
        }
    }

    suspend fun addAllPairTradeHistoriesToDb(pairTradeHistories: List<PairTradeHistory>) : DbOperation {
        return try {
            val tradeTables = ArrayList<PairHistoryTable>()
            pairTradeHistories?.forEach {
               // tradeTables.add(it.toDbTable())
                addPairTradeHistoryToDb(it)
            }
            //database.pairHistoryDAO.insertAll(tradeTables)
            DbOperation(true)
        }
        catch (ex: Exception){
            DbOperation(false, "$ex")
        }
    }

}