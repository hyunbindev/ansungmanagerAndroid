package com.example.ansungmanager.view.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ansungmanager.adapter.ProductListAdapter
import com.example.ansungmanager.data.dto.CategoryDto
import com.example.ansungmanager.databinding.FragmentProductSelectorBinding
import com.example.ansungmanager.viewmodel.CategoryViewModel
import com.example.ansungmanager.viewmodel.OrderViewModel
import com.example.ansungmanager.viewmodel.ProductViewModel
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentProductSelector : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentProductSelectorBinding;
    private lateinit var productViewModel: ProductViewModel;
    private lateinit var categoryViewModel: CategoryViewModel;
    private val orderViewModel: OrderViewModel by activityViewModels();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductSelectorBinding.inflate(layoutInflater);
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        categoryViewModel.getCategoryList();
        productViewModel.fetchProductList();

        categoryViewModel.categoryLiveData.observe(this, Observer {
            categoryList->
            updateTabs(categoryList);
        })
        productViewModel.productLiveData.observe(this, Observer{
            productList->
            binding.productList.adapter = ProductListAdapter(productList,{productDto->orderViewModel.addOrder(productDto);parentFragmentManager.popBackStack()});
            binding.productList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false);
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root;
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FragmentProductSelector().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun updateTabs(categoryDtos:List<CategoryDto>){
        binding.tabCategory.removeAllTabs();
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("전체 보기").setTag(-1L));
        categoryDtos.forEach{categoryDto->
            binding.tabCategory.addTab(binding.tabCategory.newTab().setText(categoryDto.name).setTag(categoryDto.id))
        }
    }
}