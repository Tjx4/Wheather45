package com.platform45.weather45.networking.retrofit

import com.platform45.weather45.models.Conversion
import com.platform45.weather45.models.Historical
import retrofit2.http.*

interface RetrofitHelper {
    @GET("apiconvert")
    suspend fun convert(@Query("api_key") apiKey: String, @Query("from") from: String, @Query("to") to: String, @Query("amount")  amount: String): Conversion?

    @GET("apihistorical")
    suspend fun historical(@Query("api_key") apiKey: String, @Query("date") date: String, @Query("interval") interval: String, @Query("currency") currency: String): Historical?
}