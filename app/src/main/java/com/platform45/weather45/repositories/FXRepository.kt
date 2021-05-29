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
            Conversion(0.0, 0, 0.0, null, null)
        }
    }

    suspend fun getHistorical(api_key: String, date: String, currency: String, interval: String): Historical?{
        return try {
            retrofitHelper.historical(api_key, date, interval, currency)
        }
        catch (ex: Exception){
            Historical(null, null)
        }
    }

    suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Any?{
        return try {
            retrofitHelper.series(apiKey, startDate, endDate, currency, format)
        }
        catch (ex: Exception){
            Error("null", "null")
        }
    }

    suspend fun getUSDCurrencyPairs(apiKey: String) : Currencies?{
        return try {
            retrofitHelper.currencies(apiKey)
        }
        catch (ex: Exception) {
            null
        }
    }
}