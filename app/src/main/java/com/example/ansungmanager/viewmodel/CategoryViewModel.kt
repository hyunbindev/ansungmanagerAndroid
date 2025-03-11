package com.example.ansungmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ansungmanager.data.dto.CategoryDto
import com.example.ansungmanager.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel:ViewModel() {
    private val service = RetrofitClient.service;
    private val categoryList = MutableLiveData<List<CategoryDto>>();
    val categoryLiveData:LiveData<List<CategoryDto>> get() = categoryList;

    fun getCategoryList(){
        service.getCategory().enqueue(object : Callback<List<CategoryDto>>{
            override fun onResponse(
                call: Call<List<CategoryDto>>,
                response: Response<List<CategoryDto>>
            ) {
                if(response.isSuccessful){
                    categoryList.value = response.body();
                }
            }

            override fun onFailure(call: Call<List<CategoryDto>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}