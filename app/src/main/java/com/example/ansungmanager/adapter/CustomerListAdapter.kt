package com.example.ansungmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ansungmanager.data.Customer
import com.example.ansungmanager.data.dto.CustomerDto
import com.example.ansungmanager.databinding.ItemCustomerRecyclerviewBinding

class CustomerListAdapter(val customerList: List<CustomerDto> , private val onItemClick: ((CustomerDto) -> Unit)?=null): RecyclerView.Adapter<CustomerListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListAdapter.Holder {
        val binding = ItemCustomerRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: CustomerListAdapter.Holder, position: Int,) {
        holder.tel.text = customerList[position].tel
        holder.address.text = customerList[position].jibunAddress
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(customerList[position])
        }
    }

    override fun getItemCount(): Int {
        return customerList.size;
    }
    inner class Holder(val binding :ItemCustomerRecyclerviewBinding) :RecyclerView.ViewHolder(binding.root){
        val tel = binding.customerTel
        val address = binding.customerAddress
    }
}