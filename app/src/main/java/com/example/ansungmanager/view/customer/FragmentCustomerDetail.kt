package com.example.ansungmanager.view.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.replace
import com.example.ansungmanager.R
import com.example.ansungmanager.databinding.FragmentCustomerDetailBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.retrofit.RetrofitService
import com.example.ansungmanager.view.delivery.FragmentOrderDelivery
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentCustomerDetail : Fragment(), OnMapReadyCallback {
    private lateinit var binding:FragmentCustomerDetailBinding
    private lateinit var mapView:MapView;
    private var location:LatLng?=null;
    private var marker: Marker?=null;
    private val service = RetrofitClient.service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCustomerDetailBinding.inflate(layoutInflater)
        binding.customerDeleteBtn.setOnClickListener {
            this.context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setTitle("고객정보 삭제")
                    .setMessage("한번 삭제되면 복구 할 수 없습니다.")
                    .setPositiveButton("삭제") { dialog, which ->
                        // "삭제" 버튼을 클릭했을 때 실행되는 코드
                        val customerId: Long? = arguments?.getLong("customerId")
                        if (customerId != null) {
                            // 삭제 로직 호출
                            deleteCustomer(customerId)
                        }
                    }
                    .setNegativeButton("취소") { dialog, which ->
                        // "취소" 버튼을 클릭했을 때 실행되는 코드
                        dialog.dismiss() // 대화상자 닫기
                    }
                    .show()
            }
        }
        binding.orderDelivery.setOnClickListener {

            val orderDelivery:FragmentOrderDelivery = FragmentOrderDelivery.newInstance(arguments?.getString("tel"),arguments?.getString("roadAddress"),arguments?.getLong("customerId"))
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,orderDelivery)
                .addToBackStack(null)
                .commit()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mapView = binding!!.mapView
        arguments?.let {
            binding.custerTel.text = it.getString("tel")
            binding.customerJibunAddress.text = it.getString("jibunAddress")
            binding.customerRoadAddress.text = it.getString("roadAddress")
            binding.customerRemarks.text = it.getString("remarks")
            val lat: Double? = it.getDouble("lat").takeIf { !it.isNaN() }
            val lng: Double? = it.getDouble("lng").takeIf { !it.isNaN() }
            location = if (lat != null && lng != null) {
                LatLng(lat, lng)  // 유효한 값일 때만 LatLng 생성
            } else {
                null
            }
        }
        mapView.getMapAsync ( this )
        return binding.root;
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onMapReady(naverMap: NaverMap) {
        naverMap.uiSettings.isZoomControlEnabled=false
        naverMap.moveCamera(CameraUpdate.scrollTo(location!!))
        val marker:Marker = Marker()
        if(location == null){
            binding?.mapView?.visibility=View.GONE;
        }else{
            marker.position=location!!
            marker.map = naverMap;
            this.marker = marker;
        }
    }
    private fun deleteCustomer(customerId:Long?){
        if(customerId==null){
            Toast.makeText(context,"어플리케이션 장애 다시 시도해 주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        service.deleteCustomer(customerId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Toast.makeText(context,"고객정보가 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    requireActivity().supportFragmentManager.popBackStack()
                }else{
                    Toast.makeText(context,response.body().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context,"네트워크 통신이 원활하지 않습니다",Toast.LENGTH_SHORT).show();
            }

        })
    }
}