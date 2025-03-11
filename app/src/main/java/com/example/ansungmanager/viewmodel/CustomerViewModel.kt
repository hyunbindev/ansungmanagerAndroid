package com.example.ansungmanager.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ansungmanager.data.Customer
import com.example.ansungmanager.data.dto.AddressDto
import com.example.ansungmanager.data.dto.CustomerDto
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.retrofit.RetrofitService
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerViewModel : ViewModel() {
    private val service = RetrofitClient.service;

    private val customerList = MutableLiveData<List<CustomerDto>>()
    val customersLiveData: LiveData<List<CustomerDto>> get() = customerList;

    fun fectchCustomerList(context: Context){
        service.getCustomer().enqueue(object : retrofit2.Callback<List<CustomerDto>>{
            override fun onResponse(
                call: Call<List<CustomerDto>>,
                response: Response<List<CustomerDto>>
            ) {
                if(response.isSuccessful){
                    customerList.value =response.body();
                }else{
                    customerList.value = emptyList()
                }
            }
            override fun onFailure(call: Call<List<CustomerDto>>, t: Throwable) {
                customerList.value = emptyList()
                Toast.makeText(context,"서버와 통신이 원활하지 않습니다.",Toast.LENGTH_SHORT).show();
            }
        })
    }
    fun getGeoCoder(address: String, callback:(AddressDto)->Unit) {
        service.getGeoCode(address).enqueue(object : retrofit2.Callback<AddressDto> {
            override fun onResponse(
                call: Call<AddressDto>,
                response: Response<AddressDto>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { callback(it) }
                } else {

                }
            }

            override fun onFailure(call: Call<AddressDto>, t: Throwable) {

            }
        })
    }
}