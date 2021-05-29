package com.platform45.weather45.networking.retrofit

import com.platform45.weather45.models.Conversion
import com.platform45.weather45.models.Currencies
import com.platform45.weather45.models.Historical
import com.platform45.weather45.models.Series
import retrofit2.http.*

interface RetrofitHelper {
    @GET("apiconvert")
    suspend fun convert(@Query("api_key") apiKey: String, @Query("from") from: String, @Query("to") to: String, @Query("amount")  amount: String): Conversion?

    @GET("apihistorical")
    suspend fun historical(@Query("api_key") apiKey: String, @Query("date") date: String, @Query("interval") interval: String, @Query("currency") currency: String): Historical?

    @GET("apitimeseries")
    suspend fun series(@Query("api_key") apiKey: String, @Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("currency") currency: String, @Query("format") format: String): Any?

    @GET("apicurrencies")
    suspend fun currencies(@Query("api_key") apiKey: String): Currencies?

}