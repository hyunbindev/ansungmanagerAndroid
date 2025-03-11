package com.example.ansungmanager.view.delivery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.ansungmanager.R
import com.example.ansungmanager.databinding.FragmentDeliveryMapBinding
import com.example.ansungmanager.viewmodel.DeliveryViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentDeliveryMap.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentDeliveryMap : Fragment(R.layout.fragment_delivery_map) , OnMapReadyCallback {
    private lateinit var binding :FragmentDeliveryMapBinding;
    private lateinit var mapView:MapView;
    private lateinit var naverMap: NaverMap;
    private val deliveryViewModel: DeliveryViewModel by activityViewModels();
    private var markers:MutableList<Marker> = mutableListOf();
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDeliveryMapBinding.inflate(layoutInflater);
        mapView=binding.mapView;
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapView.getMapAsync { this }
        return binding.root;
    }
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.uiSettings.isZoomControlEnabled=false;
        // 마커 리스트 초기화
        markers.clear() // 기존 마커 리스트 비우기

        // 데이터 업데이트 시 마커를 지도에 추가
        deliveryViewModel.pendingDeliveryLiveData.observe(viewLifecycleOwner, Observer { deliveryList ->
            // 기존 마커를 지우기
            markers.forEach { marker -> marker.map = null }

            // 새로운 마커 추가
            deliveryList.forEach { delivery ->
                val marker = Marker()
                marker.position = LatLng(delivery.lat, delivery.lng)
                Log.d("location", delivery.toString())

                // 마커를 지도에 추가
                marker.map = naverMap

                // 마커 리스트에 추가
                markers.add(marker)
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentDeliveryMap.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDeliveryMap().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}