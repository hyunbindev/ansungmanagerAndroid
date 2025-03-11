package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName

data class DeliveryOrderDto(
    @SerializedName("customerId")
    var customerId:Long,
    @SerializedName("orders")
    var productOrders:List<ProductOrderDto>
)
