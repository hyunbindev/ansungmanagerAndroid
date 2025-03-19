package com.example.ansungmanager.view.delivery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ansungmanager.R
import com.example.ansungmanager.adapter.PendingDeliveryProductAdapter
import com.example.ansungmanager.data.dto.PendingDeliveryDto
import com.example.ansungmanager.databinding.FragmentDeliveryDetailBinding
import com.example.ansungmanager.databinding.FragmentDeliveryListBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.viewmodel.DeliveryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val deliveryId = "param1"

class FragmentDeliveryDetail : BottomSheetDialogFragment() , OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var deliveryId: String? = null
    private lateinit var binding:FragmentDeliveryDetailBinding;
    private val service = RetrofitClient.deliveryService;
    private lateinit var mapView:MapView;
    private val deliveryViewModel: DeliveryViewModel by activityViewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deliveryId = it.getString("deliveryId")
        }
        binding = FragmentDeliveryDetailBinding.inflate(layoutInflater)
        mapView=binding.mapView;
        binding.completeDelivery.setOnClickListener{
            service.completeDelivery(deliveryId!!).enqueue(object :Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        Toast.makeText(requireContext(),"배달이 완료 되었습니다",Toast.LENGTH_SHORT).show();
                        deliveryViewModel.getPendingDeliveryList();
                        dismiss()
                    }else{
                        Toast.makeText(requireContext(),response.body().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(),"통신이 원활하지 않습니다.",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapView.getMapAsync ( this )
        return binding.root;
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(deliveryId: String) =
            FragmentDeliveryDetail().apply {
                arguments = Bundle().apply {
                    putString("deliveryId", deliveryId)
                }
            }
    }
    private fun loadDeliveryData(naverMap: NaverMap) {
        deliveryId?.let {
            service.getPendingDeliveryDetail(it).enqueue(object : Callback<PendingDeliveryDto> {
                override fun onResponse(
                    call: Call<PendingDeliveryDto>,
                    response: Response<PendingDeliveryDto>
                ) {
                    if (response.isSuccessful) {
                        val pendingDeliveryDto: PendingDeliveryDto? = response.body()
                        binding.deliveryAddress.text = pendingDeliveryDto?.roadAddress
                        binding.deliveryCustomerTel.text = pendingDeliveryDto?.customerTel
                        binding.deliveryRemarks.text = pendingDeliveryDto?.remarks
                        var totalPrice:Int = 0;
                        pendingDeliveryDto?.products?.map{ product->
                            totalPrice+= product.price?.times(product.quantity) ?: 0;
                        }
                        binding.totalPrice.text = totalPrice.toString();
                        if (pendingDeliveryDto != null) {
                            if(pendingDeliveryDto.lat == 0.0 || pendingDeliveryDto.lng == 0.0){
                                binding.mapView.visibility = View.GONE;
                            }else{
                                val location = LatLng(pendingDeliveryDto.lat,pendingDeliveryDto.lng)
                                naverMap.moveCamera(CameraUpdate.scrollTo(location))
                                var marker: Marker = Marker()
                                marker.position = location;
                                marker.map = naverMap;
                            }
                        }
                        binding.productList.adapter = pendingDeliveryDto?.let { it ->
                            PendingDeliveryProductAdapter(it)
                        }
                        binding.productList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
                    } else {
                        Toast.makeText(requireContext(), "오류 발생: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PendingDeliveryDto>, t: Throwable) {
                    Toast.makeText(requireContext(), "통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        naverMap.uiSettings.isZoomControlEnabled=false;
        loadDeliveryData(naverMap);
    }
}