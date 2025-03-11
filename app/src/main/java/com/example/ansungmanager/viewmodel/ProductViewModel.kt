package com.example.ansungmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ansungmanager.data.dto.ProductDto
import com.example.ansungmanager.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {
    private val service = RetrofitClient.service;
    private val productList = MutableLiveData<List<ProductDto>>();
    val productLiveData: LiveData<List<ProductDto>> get() = productList;
    
    fun fetchProductList(){
        service.getProducts().enqueue(object :Callback<List<ProductDto>>{
            override fun onResponse(
                call: Call<List<ProductDto>>,
                response: Response<List<ProductDto>>
            ) {
                if(response.isSuccessful){
                    productList.value = response.body();
                }else{
                    //요청실패시
                }
            }

            override fun onFailure(call: Call<List<ProductDto>>, t: Throwable) {
                //클라이언트 실패
            }
        })
    }
    fun getProductByCategory(categoryId:Long){
        service.getProductsByCategory(categoryId).enqueue(object : Callback<List<ProductDto>>{
            override fun onResponse(
                call: Call<List<ProductDto>>,
                response: Response<List<ProductDto>>
            ) {
                if(response.isSuccessful){
                    productList.value = response.body();
                }else{
                    //요청실패
                }
            }

            override fun onFailure(call: Call<List<ProductDto>>, t: Throwable) {
                TODO("Not yet implemented")
                    //클라이언트 실패
            }
        })
    }
}