package com.example.ansungmanager.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.ansungmanager.data.dto.PendingDeliveryDto
import com.example.ansungmanager.databinding.ItemPendingDeliveryBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PendingDeliveryAdapter(val pendingDeliveryList:List<PendingDeliveryDto>,val onItemClick:((PendingDeliveryDto)->Unit)?=null):RecyclerView.Adapter<PendingDeliveryAdapter.Holder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingDeliveryAdapter.Holder {
        val binding = ItemPendingDeliveryBinding.inflate(LayoutInflater.from(parent.context),parent,false);
        return Holder(binding);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PendingDeliveryAdapter.Holder, position: Int) {
        holder.address.text = pendingDeliveryList[position].roadAddress
        holder.tel.text = pendingDeliveryList[position].customerTel

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formattedDate = try {
            val createdDate = LocalDateTime.parse(pendingDeliveryList[position].createdDate)
            createdDate.format(formatter)
        } catch (e: Exception) {
            // 변환 실패 시 기본 메시지 반환
            "날짜 없음"
        }
        holder.createdTime.text = formattedDate
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(pendingDeliveryList[position]);
        }
    }

    override fun getItemCount(): Int {
        return pendingDeliveryList.size;
    }
    inner class Holder(val binding:ItemPendingDeliveryBinding):RecyclerView.ViewHolder(binding.root){
        val address = binding.deliveryAddress;
        val tel = binding.deliveryCustomerTel;
        val createdTime = binding.deliveryCreateTime;
    }
}