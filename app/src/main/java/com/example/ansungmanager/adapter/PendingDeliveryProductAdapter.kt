package com.example.ansungmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ansungmanager.data.dto.OrderDto
import com.example.ansungmanager.data.dto.PendingDeliveryDto
import com.example.ansungmanager.databinding.ItemDeliveryProductListBinding

class PendingDeliveryProductAdapter(val deliveryOrder:PendingDeliveryDto):RecyclerView.Adapter<PendingDeliveryProductAdapter.Holder>() {
    inner class Holder(val binding:ItemDeliveryProductListBinding):RecyclerView.ViewHolder(binding.root){
        val productName = binding.productName;
        val productSize = binding.productSize;
        val productQuantity = binding.productQauntity;
        val productPrice = binding.productTotalPrice;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDeliveryProductListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding);
    }

    override fun getItemCount(): Int {
        return deliveryOrder.products.size;
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.productName.text = deliveryOrder.products[position].name;
        holder.productQuantity.text = deliveryOrder.products[position].quantity.toString()+" 개";
        holder.productSize.text = deliveryOrder.products[position].size;
        holder.productPrice.text = (deliveryOrder.products[position].price?.times(deliveryOrder.products[position].quantity)).toString()+" 원";
    }
}