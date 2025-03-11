package com.example.ansungmanager.view.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ansungmanager.R
import com.example.ansungmanager.adapter.ProductListAdapter
import com.example.ansungmanager.data.dto.CategoryDto
import com.example.ansungmanager.data.dto.ProductDto
import com.example.ansungmanager.databinding.FragmentProductBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.viewmodel.CategoryViewModel
import com.example.ansungmanager.viewmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProduct.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProduct : Fragment() {
    private lateinit var binding: FragmentProductBinding;
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryViewModel: CategoryViewModel;
    private val service = RetrofitClient.service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductBinding.inflate(layoutInflater);
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        binding.fabAddProduct.setOnClickListener{
            val fragmentAddProduct:FragmentAddProduct = FragmentAddProduct();
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragmentAddProduct)
                .addToBackStack(null)
                .commit()
        }
        categoryViewModel.getCategoryList();
        categoryViewModel.categoryLiveData.observe(this , Observer {
            categoryList->
            updateTabs(categoryList)
        })
        binding.fabAddCategory.setOnClickListener{
            val editText = EditText(this.context)
            AlertDialog.Builder(this.requireContext())
                .setTitle("분류 추가")
                .setMessage("새로운 분류 항목 이름을 입력해 주세요.")
                .setView(editText)
                .setPositiveButton("추가"){_,_->
                    if(editText.text.toString().isNotEmpty()) run {
                        val categoryName: String = editText.text.toString();
                        val cateGoryDto: CategoryDto= CategoryDto(
                            null,
                            categoryName
                        )
                        addCategory(cateGoryDto)
                    }
                }
                .setNegativeButton("취소",null)
                .show();
        }
        productViewModel.productLiveData.observe(this, Observer {
            productList->
            if(productList.isNotEmpty()){
                binding.productList.adapter = ProductListAdapter(productList);
                binding.productList.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
                binding.frameEmpty.visibility=View.GONE;
            }else{
                binding.frameEmpty.visibility=View.VISIBLE;
            }
        })
        binding.tabCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val categoryId = tab?.tag as? Long
                if(categoryId != null && categoryId == -1L){
                    productViewModel.fetchProductList();
                }else{
                    if (categoryId != null) {
                        productViewModel.getProductByCategory(categoryId)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding.deleteCategoryBtn.setOnClickListener{
            deleteCategory();
        }
    }

    override fun onResume() {
        super.onResume()

        val selectedTabPosition = binding.tabCategory.selectedTabPosition;
        if(binding.tabCategory.getTabAt(selectedTabPosition) != null){
            val selectedCategoryId: Long = binding.tabCategory.getTabAt(selectedTabPosition)?.tag as Long;
            if(selectedCategoryId == -1L){
                productViewModel.fetchProductList()
            }else{
                productViewModel.getProductByCategory(selectedCategoryId)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root;
    }
    private fun addCategory(categoryDto: CategoryDto){
        service.addProductCategory(categoryDto).enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Toast.makeText(context,"분류항목이 추가되었습니다.",Toast.LENGTH_SHORT).show();
                    categoryViewModel.getCategoryList();
                }else{
                    Toast.makeText(context,"분류항목 추가 실패.",Toast.LENGTH_SHORT).show();
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context,"네트워크 문제 발생",Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun updateTabs(categoryDtos:List<CategoryDto>){
        binding.tabCategory.removeAllTabs();
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("전체 보기").setTag(-1L));
        categoryDtos.forEach{categoryDto->
            binding.tabCategory.addTab(binding.tabCategory.newTab().setText(categoryDto.name).setTag(categoryDto.id))
        }
    }
    private fun setTabLayout(){
        binding.tabCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun deleteCategory(){
        val selectedTabPosition = binding.tabCategory.selectedTabPosition;
        if(binding.tabCategory.getTabAt(selectedTabPosition) != null){
            val selectedCategoryId: Long = binding.tabCategory.getTabAt(selectedTabPosition)?.tag as Long;
            if(selectedCategoryId == -1L){
                Toast.makeText(requireContext(),"전체분류는 삭제할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }else{
                service.deleteCategory(selectedCategoryId).enqueue(object : Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            categoryViewModel.getCategoryList();
                        }else{
                            //실패 excpetion 처리
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(),"통신이 원활하지 않습니다. 잠시후 다시 시도해주세요.",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}