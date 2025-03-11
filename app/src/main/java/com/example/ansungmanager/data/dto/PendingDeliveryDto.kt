package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PendingDeliveryDto (
    @SerializedName("deliveryId")
    val deliveryId:String,
    @SerializedName("roadAddress")
    val roadAddress:String,
    @SerializedName("customerTel")
    val customerTel:String,
    @SerializedName("lat")
    val lat:Double,
    @SerializedName("lng")
    val lng:Double,
    @SerializedName("createdDate")
    val createdDate:String,
    @SerializedName("products")
    val products:List<product>,
    @SerializedName("remarks")
    val remarks:String
)
data class product(
    @SerializedName("productName")
    val name:String,
    @SerializedName("price")
    val price:Int,
    @SerializedName("size")
    val size:String,
    @SerializedName("quantity")
    val quantity:Int
)