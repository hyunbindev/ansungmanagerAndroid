package com.example.ansungmanager.view.customer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.lifecycle.ViewModelProvider
import com.example.ansungmanager.data.dto.AddressDto
import com.example.ansungmanager.data.dto.CustomerDto
import com.example.ansungmanager.databinding.FragmentAddCustomerBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.viewmodel.CustomerViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentAddCustomer : Fragment(), OnMapReadyCallback {
    private lateinit var binding : FragmentAddCustomerBinding
    private lateinit var mapView: MapView
    private lateinit var customerViewModel: CustomerViewModel
    private val service = RetrofitClient.service
    private var currentMarker: Marker? = null
    private var currentLocation:LatLng?=null
    private var jibunAddress:String?=null
    private var roadAddress:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customerViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddCustomerBinding.inflate(inflater,container,false)
        mapView = binding.mapView
        mapView.getMapAsync(this)
        binding.addCustomerBtn.setOnClickListener{
            val customerTel:String = binding.customerTel.text.toString();
            val customerAddress:String = binding.customerAddress.text.toString();
            val customerRemarks:String = binding.customerRemarks.text.toString();
            if(customerTel.isNotEmpty()){
                var customerDto:CustomerDto;
                if(roadAddress==null){
                    customerDto= CustomerDto(
                        null,
                        customerTel,
                        customerAddress,
                        customerAddress,
                        customerRemarks,
                        null,
                        null
                    )
                }else{
                    customerDto=CustomerDto(
                        null,
                        customerTel,
                        this.jibunAddress,
                        this.roadAddress,
                        customerRemarks,
                        this.currentLocation?.latitude,
                        this.currentLocation?.longitude
                    )
                }
                submitAddCustomer(customerDto);
            }else{
                Toast.makeText(this.context,"전화번호는 필수 입력사항 입니다.",Toast.LENGTH_SHORT).show();
            }
        }

        return binding.root;
    }
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        naverMap.uiSettings.isZoomControlEnabled = false
        binding.customerAddress.setOnEditorActionListener{v,actionId,event->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                Toast.makeText(this.context,binding.customerAddress.text.toString(),Toast.LENGTH_SHORT).show();
                val addressString:String = binding.customerAddress.text.toString()

                customerViewModel.getGeoCoder(addressString){
                    addressDto: AddressDto ->
                    jibunAddress=null;
                    roadAddress=null;
                    currentMarker=null;
                    currentLocation=null;
                    val location:LatLng = LatLng(addressDto.lat,addressDto.lng)
                    naverMap.moveCamera(CameraUpdate.scrollTo(location))
                    val marker = Marker()
                    marker.position = location;
                    marker.map = naverMap;
                    currentMarker=marker;
                    currentLocation=location;

                    jibunAddress=addressDto.jibunAddress;
                    roadAddress=addressDto.roadAddress;
                }
                hideKeyboard(v)
                true
            }else{
                false
            }
        }
    }
    fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun submitAddCustomer(customerDto:CustomerDto){
        service.addCustomer(customerDto).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "고객이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context, "서버 오류 발생", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}