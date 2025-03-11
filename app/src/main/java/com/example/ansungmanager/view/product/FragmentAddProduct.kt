package com.example.ansungmanager.view.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ansungmanager.R
import com.example.ansungmanager.data.dto.CategoryDto
import com.example.ansungmanager.data.dto.ProductDto
import com.example.ansungmanager.databinding.FragmentAddProductBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.viewmodel.CategoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAddProduct.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAddProduct : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val service = RetrofitClient.service
    private lateinit var categoryViewModel:CategoryViewModel;
    private lateinit var binding:FragmentAddProductBinding;
    private var categoryDto:CategoryDto?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java);
        binding = FragmentAddProductBinding.inflate(layoutInflater);
        binding.productSubmitBtn.setOnClickListener{
            if(binding.productName.text.toString().isEmpty()){
                Toast.makeText(requireContext(),"상품 이름을 입력해 주세요.",Toast.LENGTH_SHORT).show();
            }else if (categoryDto == null){
                Toast.makeText(requireContext(),"분류가 선택되지 않았어요 분류를 생성하거나 선택해 주세요.",Toast.LENGTH_SHORT).show();
            }else{
                submitAddProduct()
            }
        }
        //상품 카테고리
        categoryViewModel.getCategoryList();
        categoryViewModel.categoryLiveData.observe(this) { categoryList ->
            categoryList?.let {
                val categoryNames = it.map { category -> category.name }
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_dropdown_category_item,
                    categoryNames
                )
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_category_item)
                binding.spinnerCategory.adapter = adapter
            }
        }
        binding.spinnerCategory.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryDto = categoryViewModel.categoryLiveData.value?.get(position);
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return this.binding.root;
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAddProduct().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun submitAddProduct(){
        var productDto: ProductDto=ProductDto(
            null,
            binding.productName.text.toString(),
            categoryDto,
            binding.productSize.text.toString(),
            binding.productPrice.text.toString().toInt()
        )
        service.addProduct(productDto).enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Toast.makeText(context, "상품이 추가되었습니다",Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack();
                }else{
                    Toast.makeText(context,"상품을 추가하지 못했습니다 다시 시도해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context,"네트워크 오류",Toast.LENGTH_SHORT).show();
            }
        })
    }
}