package com.example.ansungmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ansungmanager.data.dto.CustomerDto
import com.example.ansungmanager.data.dto.ProductDto
import com.example.ansungmanager.databinding.ItemProductRecyclerviewBinding
import java.text.NumberFormat
import java.util.Locale

class ProductListAdapter(val productList:List<ProductDto>, private val onItemClick: ((ProductDto) -> Unit)?=null):RecyclerView.Adapter<ProductListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListAdapter.Holder {
        val binding = ItemProductRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: ProductListAdapter.Holder, position: Int) {
        holder.productName.text = productList[position].name;
        holder.productSize.text = productList[position].size;
        holder.productPrice.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(productList[position].price.toInt()).replace("₩", "").trim()+"원";
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(productList[position]);
        }
    }

    override fun getItemCount(): Int {
        return productList.size;
    }
    inner class Holder(val binding:ItemProductRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){
        val productName = binding.productName;
        val productSize= binding.productSize;
        val productPrice = binding.productPrice;
    }
}