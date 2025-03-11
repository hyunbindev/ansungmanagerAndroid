package com.example.ansungmanager.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://1.231.178.91:8080/"

    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val service: RetrofitService by lazy{
        retrofit.create(RetrofitService::class.java)
    }

    val deliveryService: RetrofitDeliveryService by lazy{
        retrofit.create(RetrofitDeliveryService::class.java)
    }
}