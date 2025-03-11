package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName

data class CustomerDto(
    @SerializedName("id")
    val id: Long?,

    @SerializedName("tel")
    val tel:String,

    @SerializedName("jibunAddress")
    val jibunAddress:String?,

    @SerializedName("roadAddress")
    val roadAddress:String?,

    @SerializedName("remarks")
    val remarks:String?,

    @SerializedName("lat")
    val lat:Double?,

    @SerializedName("lng")
    val lng:Double?
)
