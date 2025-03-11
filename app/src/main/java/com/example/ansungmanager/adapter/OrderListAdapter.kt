package com.example.ansungmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ansungmanager.data.dto.OrderDto
import com.example.ansungmanager.databinding.ItemOrderProductItemBinding
import java.text.NumberFormat
import java.util.Locale

class OrderListAdapter(
    val orderList:List<OrderDto>,
    private val onIncrement: (Long?) -> Unit,
    private val onDecrement: (Long?) -> Unit,
    private val onRemove: (Long?) -> Unit
):RecyclerView.Adapter<OrderListAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.Holder {
        val binding = ItemOrderProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: OrderListAdapter.Holder, position: Int) {
        holder.productName.text = orderList[position].product.name;
        holder.productPrice.text = convertCurrencyFormat(orderList[position].product.price);
        holder.productSize.text = orderList[position].product.size;
        holder.quantity.text = orderList[position].quantity.toString();

        holder.quantityIncrementBtn.setOnClickListener{onIncrement(orderList[position].product.id)}
        holder.quantityDecrementBtn.setOnClickListener{onDecrement(orderList[position].product.id)}
        holder.cancelBtn.setOnClickListener{onRemove(orderList[position].product.id)}
    }

    override fun getItemCount(): Int {
        return orderList.size;
    }
    inner class Holder(val binding:ItemOrderProductItemBinding):RecyclerView.ViewHolder(binding.root){
        val productName = binding.productName;
        val productPrice = binding.prodcutPrice;
        val productSize = binding.productSize;
        val quantityDecrementBtn = binding.quantityDecBtn;
        val quantity = binding.productQuantity;
        val quantityIncrementBtn = binding.quantityIncBtn
        val cancelBtn = binding.orderCancelBtn;
    }
    private fun convertCurrencyFormat(price:Int): String {
        return NumberFormat.getCurrencyInstance(Locale.KOREA).format(price).replace("₩", "").trim()+"원";
    }
}