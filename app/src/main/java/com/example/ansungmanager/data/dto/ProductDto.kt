package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id")
    val id: Long?,

    @SerializedName("name")
    val name:String,

    @SerializedName("category")
    val category:CategoryDto?,

    @SerializedName("size")
    val size:String,

    @SerializedName("price")
    val price:Int
)
