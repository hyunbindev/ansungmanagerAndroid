package com.example.ansungmanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ansungmanager.data.dto.PendingDeliveryDto
import com.example.ansungmanager.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryViewModel : ViewModel() {
    private val service = RetrofitClient.deliveryService;
    private val deliveryList = MutableLiveData<List<PendingDeliveryDto>>();
    val pendingDeliveryLiveData : LiveData<List<PendingDeliveryDto>> get() = deliveryList;

    fun getPendingDeliveryList(){
        service.getPendingDelivery().enqueue(object : Callback<List<PendingDeliveryDto>> {
            override fun onResponse(
                call: Call<List<PendingDeliveryDto>>,
                response: Response<List<PendingDeliveryDto>>
            ) {
                if(response.isSuccessful){
                    deliveryList.value = response.body();
                    Log.d("@@@@@@@@@@@@@@@@@@@@@@@",response.body().toString())
                }else{
                    Log.d("에러남","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                    deliveryList.value = emptyList();
                }
            }
            override fun onFailure(call: Call<List<PendingDeliveryDto>>, t: Throwable) {
                deliveryList.value = emptyList();
                Log.d("에러남@@@@@@@@@@@@@",t.toString())
            }
        })
    }
}