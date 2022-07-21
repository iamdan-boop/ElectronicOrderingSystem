package com.sti.sticanteen.presentation.dashboard.screens.products.detail.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.domain.repository.AuthenticationRepository
import com.sti.sticanteen.domain.repository.CartRepository
import com.sti.sticanteen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


const val PRODUCT_QUANTITY_SAVED_STATE_KEY = "productQuantity"

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {


    private val _productDetailState = mutableStateOf(ProductDetailState())
    val productDetailState = _productDetailState

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState


    init {
        savedStateHandle.get<Int>(PRODUCT_QUANTITY_SAVED_STATE_KEY)?.let { savedStateQuantity ->
            _productDetailState.value = _productDetailState.value.copy(
                quantity = savedStateQuantity
            )
        }
    }


    fun addProductToCart(productId: Long) = viewModelScope.launch {
//        if (_productDetailState.value.quantity == 0) return@launch
        cartRepository.addProductToCart(
            productId = productId,
        ).collect { addProductResource ->
            when (addProductResource) {
                is Resource.Error -> {
                    _productDetailState.value = _productDetailState.value.copy(isLoading = false)
                    _uiState.value =
                        ProductDetailUiState.Error(message = addProductResource.message!!)
                }
                is Resource.Loading -> {
                    _productDetailState.value = _productDetailState.value.copy(isLoading = true)
                    _uiState.value =
                        ProductDetailUiState.Loading
                }
                is Resource.Success -> {
                    _productDetailState.value = _productDetailState.value.copy(isLoading = false)
                    _uiState.value = ProductDetailUiState.Success
                }
            }
        }
    }


    fun productQuantityChanged(quantityString: String) = viewModelScope.launch {
        if (quantityString.isBlank()) return@launch
        val current = Integer.parseInt(quantityString)
        if (current > 10) {
            return@launch
        }
        _productDetailState.value =
            _productDetailState.value.copy(
                quantity = current
            )
        storeToSavedState(quantity = current)
    }


    fun clearProductQuantity() {
        _productDetailState.value = _productDetailState.value.copy(quantity = 0)
    }


    fun incrementProduct() = viewModelScope.launch {
        if (_productDetailState.value.quantity == 10) {
            return@launch
        }
        _productDetailState.value = _productDetailState.value.copy(
            quantity = _productDetailState.value.quantity + 1
        )
        storeToSavedState(quantity = _productDetailState.value.quantity)
    }


    private fun storeToSavedState(quantity: Int) {
        savedStateHandle.set(PRODUCT_QUANTITY_SAVED_STATE_KEY, quantity)
    }
}