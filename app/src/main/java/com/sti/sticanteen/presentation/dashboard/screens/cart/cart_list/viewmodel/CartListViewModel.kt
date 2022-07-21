package com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.domain.repository.CartRepository
import com.sti.sticanteen.utils.Resource
import com.sti.sticanteen.utils.Resource.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartListViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartState = mutableStateOf(CartListState())
    val cartState = _cartState

    private val _uiState = MutableStateFlow<Resource<Unit>>(Loading(Unit))
    val uiState: StateFlow<Resource<Unit>> = _uiState


    init {
        getCartProducts()
    }


    fun onEvent(event: CartListEvent) {
        when (event) {
            CartListEvent.ClearSelectedProducts ->
                _cartState.value = _cartState.value.copy(
                    selectedProducts = emptyList()
            )
            is CartListEvent.ProductClick -> productClick(event.product)
            is CartListEvent.ProductLongClick -> onProductLongClick(event.product)
            CartListEvent.SelectAllProducts -> onSelectAllProducts()
        }
    }


    private fun getCartProducts() = viewModelScope.launch {
        cartRepository.getProducts().collect { cartProductResource ->
            when (cartProductResource) {
                is Resource.Error -> {
                    _cartState.value = _cartState.value.copy(
                        isLoading = false,
                        products = cartProductResource.data ?: emptyList(),
                        selectedProducts = _cartState.value.selectedProducts
                    )
                }
                is Loading -> _cartState.value = _cartState.value.copy(
                    isLoading = true,
                    products = cartProductResource.data ?: emptyList(),
                    selectedProducts = _cartState.value.selectedProducts
                )
                is Resource.Success -> _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    products = cartProductResource.data ?: emptyList(),
                    selectedProducts = _cartState.value.selectedProducts
                )
            }
        }
    }


    private fun productClick(product: Product) {
        if (_cartState.value.selectedProducts.isEmpty()) return
        if (!_cartState.value.selectedProducts.contains(product)) {
            val mutateSelectedProducts = _cartState.value.selectedProducts.toMutableList()
                .also { mutableProducts -> mutableProducts.add(product) }
            _cartState.value = _cartState.value.copy(
                selectedProducts = mutateSelectedProducts
            )
            return
        }

        val newSelectedProducts = ArrayList(_cartState.value.selectedProducts)
        newSelectedProducts.remove(product)
        _cartState.value = _cartState.value.copy(
            selectedProducts = newSelectedProducts,
        )
    }


    private fun onSelectAllProducts() {
        val (_, products, selectedProducts) = _cartState.value
        if (selectedProducts.toTypedArray() contentDeepEquals products.toTypedArray()) return
        _cartState.value = _cartState.value.copy(
            selectedProducts = products
        )
    }


    private fun onProductLongClick(product: Product) {
        if (_cartState.value.selectedProducts.contains(product)) return
        val newSelectedProductList = ArrayList(_cartState.value.selectedProducts)
        newSelectedProductList.add(product)
        _cartState.value = _cartState.value.copy(
            selectedProducts = newSelectedProductList
        )
    }
}
