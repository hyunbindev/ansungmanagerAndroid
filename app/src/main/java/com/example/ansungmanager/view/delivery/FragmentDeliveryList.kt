package com.example.ansungmanager.view.delivery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ansungmanager.R
import com.example.ansungmanager.adapter.PendingDeliveryAdapter
import com.example.ansungmanager.data.dto.PendingDeliveryDto
import com.example.ansungmanager.databinding.FragmentDeliveryListBinding
import com.example.ansungmanager.viewmodel.DeliveryViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentDeliveryList : Fragment() {
    private val deliveryViewModel:DeliveryViewModel by activityViewModels();
    private lateinit var binding :FragmentDeliveryListBinding;
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDeliveryListBinding.inflate(layoutInflater);
        deliveryViewModel.pendingDeliveryLiveData.observe(this, Observer {
            deliveryList->
            binding.deliveryList.adapter = PendingDeliveryAdapter(deliveryList){deliveryDto ->onClickDelivery(deliveryDto)};
        })
        binding.deliveryList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        deliveryViewModel.getPendingDeliveryList();
        return binding.root;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDeliveryList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun onClickDelivery(deliveryDto:PendingDeliveryDto){
        val fragmentDeliveryDetail:FragmentDeliveryDetail = FragmentDeliveryDetail.newInstance(deliveryDto.deliveryId);
        fragmentDeliveryDetail.show(parentFragmentManager,"bottomSheet");
    }
}