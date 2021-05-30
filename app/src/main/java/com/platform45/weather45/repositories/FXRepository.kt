package com.platform45.weather45.repositories

import com.platform45.weather45.extensions.toPairHistoryTable
import com.platform45.weather45.extensions.toSeries
import com.platform45.weather45.models.Conversion
import com.platform45.weather45.models.Currencies
import com.platform45.weather45.models.Series
import com.platform45.weather45.networking.retrofit.RetrofitHelper
import com.platform45.weather45.persistance.room.FX45Db

class FXRepository(private val retrofitHelper: RetrofitHelper, private val database: FX45Db) {

    suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getHistorical(api_key: String, date: String, currency: String, interval: String): Any?{
        return try {
            retrofitHelper.historical(api_key, date, interval, currency)
        }
        catch (ex: Exception){
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

    suspend fun getAllSeriesFromDb() : List<Series>?{
        return try {
            val series = ArrayList<Series>()
            database.pairHistoryDAO.getAllHistories()?.forEach { series.add(it.toSeries()) }
            series
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getSeriesFromDb(id: Long) : Series?{
        return try {
            database.pairHistoryDAO.get(id)?.toSeries()
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun cacheSeries(series: Series) {
         try {
            database.pairHistoryDAO.insert(series.toPairHistoryTable())
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
}