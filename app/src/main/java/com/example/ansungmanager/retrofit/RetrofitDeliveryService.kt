package com.example.ansungmanager.retrofit

import com.example.ansungmanager.data.dto.DeliveryOrderDto
import com.example.ansungmanager.data.dto.PendingDeliveryDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitDeliveryService {
    @POST("/api/delivery")
    fun addDelivery(@Body deliveryOrderDto: DeliveryOrderDto): Call<Void>
    @GET("/api/delivery")
    fun getPendingDelivery():Call<List<PendingDeliveryDto>>
    @GET("/api/delivery/detail")
    fun getPendingDeliveryDetail(@Query("deliveryId")deliveryId:String):Call<PendingDeliveryDto>
    @POST("/api/delivery/complete")
    fun completeDelivery(@Query("deliveryId")deliveryId: String):Call<Void>
}