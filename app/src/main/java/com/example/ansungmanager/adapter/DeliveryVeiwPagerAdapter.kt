package com.example.ansungmanager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ansungmanager.view.delivery.FragmentDeliveryList
import com.example.ansungmanager.view.delivery.FragmentDeliveryMap

class DeliveryVeiwPagerAdapter(fragmentActivity: FragmentActivity) :FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int =3;

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->FragmentDeliveryList.newInstance("param1","param2");
            1->FragmentDeliveryMap.newInstance("param1","param2");
            else -> FragmentDeliveryList.newInstance("param1","param2");
        }
    }
}