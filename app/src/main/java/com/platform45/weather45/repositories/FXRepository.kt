package com.platform45.weather45.repositories

import com.platform45.weather45.models.Conversion
import com.platform45.weather45.networking.retrofit.RetrofitHelper

class FXRepository(private val retrofitHelper: RetrofitHelper) {

    suspend fun getConvertion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            Conversion(0.0, 0, 0.0, "", "")
        }
    }
}