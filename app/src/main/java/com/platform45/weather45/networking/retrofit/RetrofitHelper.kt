package com.platform45.weather45.networking.retrofit

import com.platform45.weather45.constants.DAILY
import com.platform45.weather45.constants.DETAILS
import com.platform45.weather45.models.Daily
import retrofit2.http.*

interface RetrofitHelper {
    @GET(DAILY)
    suspend fun getDaily(@Query("x-rapidapi-key") apiKey: String): List<Daily>?

    @GET(DETAILS)
    suspend fun getDetails(@Query("x-rapidapi-key") apiKey: String): List<Daily>?
}