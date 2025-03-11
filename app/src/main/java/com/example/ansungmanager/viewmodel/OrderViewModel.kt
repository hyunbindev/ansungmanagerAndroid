package com.example.ansungmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ansungmanager.data.dto.OrderDto
import com.example.ansungmanager.data.dto.ProductDto

class OrderViewModel :ViewModel() {
    private val orderList = MutableLiveData<List<OrderDto>>(emptyList());
    val orderLiveData: LiveData<List<OrderDto>> get() = orderList;

    fun addOrder(product: ProductDto) {
        val currentList = orderList.value ?: emptyList()
        val updatedList = currentList.toMutableList()

        // 이미 존재하는 제품인지 확인
        val isExisting = updatedList.any { it.product.id == product.id }

        if (!isExisting) {
            updatedList.add(OrderDto(product, 1,null))
        }
        orderList.value = updatedList
    }
    fun incrementOrderQuantity(productId:Long?){
        val currentList = orderList.value ?: emptyList()
        val updatedList = currentList.toMutableList()
        for (i in updatedList.indices) {
            if (updatedList[i].product.id == productId) {
                updatedList[i].quantity++;
                break // 해당 제품을 찾으면 반복문 종료
            }
        }
        orderList.value = updatedList;
    }
    fun decrementOrderQuantity(productId:Long?){
        val currentList = orderList.value ?: emptyList()
        val updatedList = currentList.toMutableList()
        for (i in updatedList.indices) {
            if (updatedList[i].product.id == productId) {
                updatedList[i].quantity--;
                break // 해당 제품을 찾으면 반복문 종료
            }
        }
        orderList.value = updatedList;
    }
    fun removeOrder(productId: Long?){
        val currentList = orderList.value ?: emptyList()
        val updatedList = currentList.toMutableList()
        for (i in updatedList.indices) {
            if (updatedList[i].product.id == productId) {
                updatedList.removeAt(i)
                break // 해당 제품을 찾으면 반복문 종료
            }
        }
        orderList.value = updatedList;
    }
    fun clearList(){
        orderList.value = emptyList();
    }
}