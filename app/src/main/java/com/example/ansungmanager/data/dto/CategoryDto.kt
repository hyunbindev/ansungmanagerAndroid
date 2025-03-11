package com.example.ansungmanager.data.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id")
    val id:Long?,
    @SerializedName("name")
    val name:String
)
