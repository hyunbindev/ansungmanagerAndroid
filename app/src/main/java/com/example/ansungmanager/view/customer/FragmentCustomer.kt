package com.example.ansungmanager.view.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ansungmanager.R
import com.example.ansungmanager.adapter.CustomerListAdapter
import com.example.ansungmanager.data.dto.CustomerDto
import com.example.ansungmanager.databinding.FragmentCustomerBinding
import com.example.ansungmanager.viewmodel.CustomerViewModel

class FragmentCustomer : Fragment() {
    private lateinit var binding:FragmentCustomerBinding
    private lateinit var customerViewModel: CustomerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCustomerBinding.inflate(layoutInflater)

        customerViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
        customerViewModel.customersLiveData.observe(this, Observer { customerList->
            binding.customerList.adapter = CustomerListAdapter(customerList){customerDto ->  showCustomerDetail(customerDto)}
            binding.customerList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        })
        binding.fabAddCustomer.setOnClickListener{
            val fragmentAddCustomer: FragmentAddCustomer = FragmentAddCustomer();
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragmentAddCustomer)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        customerViewModel.fectchCustomerList(this.requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
    private fun showCustomerDetail(customerDto: CustomerDto){
        val fragmentCustomerDetail: FragmentCustomerDetail = FragmentCustomerDetail();
        val bundle = Bundle()
        customerDto.id?.let { bundle.putLong("customerId", it) };
        bundle.putString("tel",customerDto.tel)
        bundle.putString("jibunAddress",customerDto.jibunAddress)
        bundle.putString("roadAddress",customerDto.roadAddress)
        bundle.putString("remarks",customerDto.remarks)
        customerDto.lat?.let { bundle.putDouble("lat", it) }
        customerDto.lng?.let { bundle.putDouble("lng",it) }
        fragmentCustomerDetail.arguments = bundle;
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragmentCustomerDetail)
            .addToBackStack(null)
            .commit()
    }
}