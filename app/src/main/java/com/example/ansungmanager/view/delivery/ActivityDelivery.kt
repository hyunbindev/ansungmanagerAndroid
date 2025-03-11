package com.example.ansungmanager.view.delivery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.ansungmanager.R
import com.example.ansungmanager.adapter.DeliveryVeiwPagerAdapter
import com.example.ansungmanager.databinding.ActivityDeliveryBinding
import com.example.ansungmanager.viewmodel.DeliveryViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ActivityDelivery : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryBinding
    private val deliveryViewModel: DeliveryViewModel by viewModels();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ViewPager2 어댑터 설정
        binding.pager.adapter = DeliveryVeiwPagerAdapter(this)
        binding.pager.setCurrentItem(0, true)

        // TabLayout + ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "목록"
                    //tab.setIcon(R.drawable.ic_delivery) // 아이콘 설정 (선택)
                    binding.pager.isUserInputEnabled = true
                }
                1 -> {
                    tab.text = "지도"
                    //tab.setIcon(R.drawable.ic_done)
                    binding.pager.isUserInputEnabled = false
                }
            }
        }.attach()
        //delivery data load
        deliveryViewModel.getPendingDeliveryList();
    }
}
