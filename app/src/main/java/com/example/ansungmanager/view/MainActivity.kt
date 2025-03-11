package com.example.ansungmanager.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.ansungmanager.R
import com.example.ansungmanager.databinding.ActivityMainBinding
import com.example.ansungmanager.view.customer.FragmentCustomer
import com.example.ansungmanager.view.delivery.ActivityDelivery
import com.example.ansungmanager.view.product.FragmentProduct
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,FragmentHome())
                .commit()
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeMenuBtn -> {
                    setFragment(FragmentHome())  // FragmentHome로 교체
                    true  // 이벤트 처리 완료
                }
                R.id.customerMenuBtn -> {
                    setFragment(FragmentCustomer())  // FragmentCustomer로 교체
                    true  // 이벤트 처리 완료
                }
                R.id.productMenuBtn -> {
                    setFragment(FragmentProduct())  // FragmentProduct로 교체
                    true  // 이벤트 처리 완료
                }
                R.id.deliveryMenuBtn -> {
                    startActivity(Intent(this, ActivityDelivery::class.java))  // ActivityDelivery로 이동
                    false  // 이벤트 처리 완료
                }
                else -> {
                    setFragment(FragmentHome())  // 기본 Fragment 설정
                    true
                }
            }
        }
    }
    private fun setFragment(frag : Fragment){
        supportFragmentManager.commit {
            replace(R.id.fragment_container,frag)
            setReorderingAllowed(true)
        }
    }
}