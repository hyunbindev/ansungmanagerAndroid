package com.example.ansungmanager.retrofit

import com.example.ansungmanager.data.dto.AddressDto
import com.example.ansungmanager.data.dto.CategoryDto
import com.example.ansungmanager.data.dto.CustomerDto
import com.example.ansungmanager.data.dto.ProductDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {
    @GET("/api/customer")
    fun getCustomer():Call<List<CustomerDto>>

    @GET("/api/geocoder")
    fun getGeoCode(@Query("address") address : String):Call<AddressDto>

    @POST("/api/customer")
    fun addCustomer(@Body customerDto:CustomerDto):Call<Void>

    @DELETE("/api/customer")
    fun deleteCustomer(@Query("customerId") customerId:Long):Call<Void>

    @GET("/api/product")
    fun getProducts():Call<List<ProductDto>>

    @GET("/api/product/category")
    fun getProductsByCategory(@Query("categoryId") categoryId:Long):Call<List<ProductDto>>

    @POST("/api/product")
    fun addProduct(@Body productDto:ProductDto) : Call<Void>

    @POST("/api/category")
    fun addProductCategory(@Body categoryDto: CategoryDto) :Call<Void>

    @GET("/api/category")
    fun getCategory():Call<List<CategoryDto>>

    @DELETE("/api/category")
    fun deleteCategory(@Query("categoryId")categoryId: Long):Call<Void>
}