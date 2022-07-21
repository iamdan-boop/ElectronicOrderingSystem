package com.sti.sticanteen.presentation.dashboard.screens.home.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.domain.repository.ProductRepository
import com.sti.sticanteen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


const val DRINK_TYPE_KEY = "drinks"
const val BEVERAGES_TYPE_KEY = "beverages"
const val SNACK_TYPE_KEY = "snacks"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _homeState = mutableStateOf(HomeProductState())
    val homeState = _homeState



    init {
        getProductsByType(1, DRINK_TYPE_KEY)
        getProductsByType(2, BEVERAGES_TYPE_KEY)
        getProductsByType(3, SNACK_TYPE_KEY)
    }


    private fun getProductsByType(type: Int, key: String) = viewModelScope.launch {
        productRepository.getProducts(type).collect { productResource ->
            when (productResource) {
                is Resource.Error -> return@collect
                is Resource.Loading -> return@collect
                is Resource.Success -> {
                    val newProductsMap = _homeState.value.products.toMutableMap()
                    newProductsMap[key] = productResource.data ?: emptyList()
                    _homeState.value = _homeState.value.copy(
                        products = newProductsMap
                    )
                }
            }
        }
    }
}