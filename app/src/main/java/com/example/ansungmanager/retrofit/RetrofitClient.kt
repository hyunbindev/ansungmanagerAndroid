package com.example.ansungmanager.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://1.231.178.91/"
    private var accessToken:String ? = null;
    private val authInterceptor = Interceptor{ chain ->
        val originalRequest: Request = chain.request()
        val newRequest = originalRequest.newBuilder()
            .apply {
                accessToken?.let{
                    header("Authorization", it)
                }
            }.build()
        chain.proceed(newRequest);
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()
    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val service: RetrofitService by lazy{
        retrofit.create(RetrofitService::class.java)
    }

    val deliveryService: RetrofitDeliveryService by lazy{
        retrofit.create(RetrofitDeliveryService::class.java)
    }
    public fun setAccessToken(accessToken : String){
        this.accessToken = accessToken;
    }
}