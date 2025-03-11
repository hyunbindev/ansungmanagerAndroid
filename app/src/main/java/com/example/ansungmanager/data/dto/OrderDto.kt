package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("product")
    val product:ProductDto,
    @SerializedName("quantity")
    var quantity:Int,
    @SerializedName("price")
    val price:Int?
)