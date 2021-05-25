package com.platform45.weather45.networking.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    var retrofit: RetrofitHelper

    init {
        val builder = Retrofit.Builder()
            .baseUrl(Hosts.Production.url)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()

        API.retrofit = retrofit.create(RetrofitHelper::class.java)
    }
}