package com.platform45.weather45.repositories

import com.platform45.weather45.models.Conversion
import com.platform45.weather45.models.Currencies
import com.platform45.weather45.models.Error
import com.platform45.weather45.models.Historical
import com.platform45.weather45.models.Series
import com.platform45.weather45.networking.retrofit.RetrofitHelper
import retrofit2.http.Query

class FXRepository(private val retrofitHelper: RetrofitHelper) {

    suspend fun getConvertion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getHistorical(api_key: String, date: String, currency: String, interval: String): Any?{
        return try {
            retrofitHelper.historical(api_key, date, interval, currency) as Historical
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Any?{
        return try {
            retrofitHelper.series(apiKey, startDate, endDate, currency, format) as Series
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getUSDCurrencyPairs(apiKey: String) : Any?{
        return try {
            retrofitHelper.currencies(apiKey)
        }
        catch (ex: Exception) {
            null
        }
    }
}