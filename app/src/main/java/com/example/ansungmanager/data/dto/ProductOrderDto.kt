package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName

data class ProductOrderDto (
    @SerializedName("productId")
    var productId:Long,
    @SerializedName("quantity")
    var quantity:Int
)