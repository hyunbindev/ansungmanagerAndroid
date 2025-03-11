package com.example.ansungmanager.view.delivery

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ansungmanager.R
import com.example.ansungmanager.adapter.OrderListAdapter
import com.example.ansungmanager.data.dto.DeliveryOrderDto
import com.example.ansungmanager.data.dto.OrderDto
import com.example.ansungmanager.data.dto.ProductOrderDto
import com.example.ansungmanager.databinding.FragmentOrderDeliveryBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.retrofit.RetrofitDeliveryService
import com.example.ansungmanager.view.product.FragmentProductSelector
import com.example.ansungmanager.viewmodel.OrderViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class FragmentOrderDelivery : Fragment() {
    private lateinit var binding:FragmentOrderDeliveryBinding;
    private val orderViewModel: OrderViewModel by activityViewModels();
    private lateinit var deliveryService: RetrofitDeliveryService;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deliveryService = RetrofitClient.deliveryService;
        binding = FragmentOrderDeliveryBinding.inflate(layoutInflater);
        arguments?.let {
            binding.customerTel.text=it.getString("customerTel")
            binding.customerAddress.text=it.getString("customerAddress")
        }
        orderViewModel.orderLiveData.observe(this, Observer{
            orderList->
            binding.orderProductList.adapter = OrderListAdapter(
                orderList,
                onIncrement = { productid -> orderViewModel.incrementOrderQuantity(productid) },
                onDecrement = { productid -> orderViewModel.decrementOrderQuantity(productid) },
                onRemove = {productid -> orderViewModel.removeOrder(productid)}
            );
            binding.orderProductList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
            var totalPrice:Int = 0;
            orderList.forEach{order->
                totalPrice += order.product.price * order.quantity;
            }
            binding.orderTotalPrice.text=convertCurrencyFormat(totalPrice);
        })

        binding.addProductBtn.setOnClickListener{
            val fragmentProductSelector:FragmentProductSelector = FragmentProductSelector.newInstance();
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragmentProductSelector)
                .addToBackStack(null)
                .commit()
        }
        binding.submitDeliveryOrder.setOnClickListener{
            submitDeliveryOrder()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root;
    }

    override fun onDestroy() {
        super.onDestroy()
        orderViewModel.clearList();
    }
    companion object {
        @JvmStatic
        fun newInstance(custeomrTel: String?, customerAddress: String? ,customerId : Long?) =
            FragmentOrderDelivery().apply {
                arguments = Bundle().apply {
                    putString("customerTel", custeomrTel)
                    putString("customerAddress", customerAddress)
                    if (customerId != null) {
                        putLong("customerId",customerId)
                    }
                }
            }
    }
    private fun convertCurrencyFormat(price:Int): String {
        return NumberFormat.getCurrencyInstance(Locale.KOREA).format(price).replace("₩", "").trim()+"원";
    }
    private fun submitDeliveryOrder(){
        val customerId:Long? = arguments?.getLong("customerId");
        val orderProducts :List<OrderDto>? = orderViewModel.orderLiveData.value;
        if(customerId !=null && orderProducts !=null){
            var productOrderDto:List<ProductOrderDto> = orderProducts.map { orderDto ->
                orderDto.product.id?.let {
                    ProductOrderDto(
                        it,
                        orderDto.quantity
                    )
                }!!
            }
            var deliveryOrderDto:DeliveryOrderDto =DeliveryOrderDto(
                customerId,
                productOrderDto
            )
            deliveryService.addDelivery(deliveryOrderDto).enqueue(object  : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        requireActivity().supportFragmentManager.popBackStack();
                        startActivity(Intent(requireContext(),ActivityDelivery::class.java))
                    }else{
                        Toast.makeText(requireContext(),"주문이 실패했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(),"통신이 원활하지 않습니다. 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                }
            })
        }
    }
}