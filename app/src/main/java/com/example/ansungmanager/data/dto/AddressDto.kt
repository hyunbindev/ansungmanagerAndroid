package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName
import com.naver.maps.geometry.LatLng

data class AddressDto(
    @SerializedName("jibunAddress")
    val jibunAddress:String,
    @SerializedName("roadAddress")
    val roadAddress:String,
    @SerializedName("x")
    val lng:Double,
    @SerializedName("y")
    val lat:Double
)
