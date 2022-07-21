package com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.domain.repository.CheckoutRepository
import com.sti.sticanteen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {

    private val _checkoutState = mutableStateOf(CheckoutState())
    val checkoutState = _checkoutState


    private val _uiState: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading())
    val uiState: StateFlow<Resource<String>> = _uiState

    fun onEvent(event: CheckoutEvent) {
        when (event) {
            is CheckoutEvent.SetSelectedProducts -> setSelectedProducts(event.selectedProducts)
            is CheckoutEvent.OnDecrementProductQuantity -> onDecrementProductQuantity(event.productCheckout)
            is CheckoutEvent.OnIncrementProductQuantity -> onIncrementProductQuantity(event.productCheckout)
            CheckoutEvent.CheckoutProducts -> checkoutProducts()
        }
    }


    private fun checkoutProducts() = viewModelScope.launch {
        checkoutRepository.checkoutProducts(checkoutProducts = _checkoutState.value.selectedProducts)
            .collect { checkoutResource ->
                when (checkoutResource) {
                    is Resource.Error -> {
                        _checkoutState.value = _checkoutState.value.copy(
                            isLoading = false
                        )
                        _uiState.emit(
                            Resource.Error(message = checkoutResource.message ?: "")
                        )
                    }
                    is Resource.Loading -> _checkoutState.value = _checkoutState.value.copy(
                        isLoading = true
                    )
                    is Resource.Success -> {
                        _checkoutState.value = _checkoutState.value.copy(
                            isLoading = false,
                            referenceNumber = checkoutResource.data!!.referenceNumber
                        )
                        _uiState.emit(Resource.Success(checkoutResource.data.referenceNumber))
                    }
                }
            }
    }


    private fun onIncrementProductQuantity(productCheckout: ProductCheckout) {
        val currentProducts = _checkoutState.value.selectedProducts
        val findCheckoutProduct =
            currentProducts.find { product -> product == productCheckout } ?: return
        val currentIndex = currentProducts.indexOf(productCheckout)


        val newProductList = currentProducts.toMutableList()
            .also { mutableProductCheckout ->
                mutableProductCheckout[currentIndex] = findCheckoutProduct.copy(
                    quantity = findCheckoutProduct.quantity + 1
                )
            }.toImmutableList()

        val newSubTotal =
            newProductList.sumOf { sumCheckout -> sumCheckout.quantity * sumCheckout.product.price }
                .toLong()

        _checkoutState.value = _checkoutState.value.copy(
            selectedProducts = newProductList,
            subTotal = newSubTotal
        )
    }

    private fun onDecrementProductQuantity(productCheckout: ProductCheckout) {
        val currentProducts = _checkoutState.value.selectedProducts
        val currentIndex = currentProducts.indexOf(productCheckout)
        val findCheckoutProduct = currentProducts.find { product -> product == productCheckout }
            ?: return
        if (findCheckoutProduct.quantity == 1) return

        val newProductList = currentProducts.toMutableList()
            .also { mutableProductCheckout ->
                mutableProductCheckout[currentIndex] = findCheckoutProduct.copy(
                    quantity = findCheckoutProduct.quantity - 1
                )
            }.toImmutableList()
        val newSubTotal =
            newProductList.sumOf { sumCheckout -> sumCheckout.quantity * sumCheckout.product.price }
                .toLong()

        _checkoutState.value = _checkoutState.value.copy(
            selectedProducts = newProductList,
            subTotal = newSubTotal
        )
    }

    private fun setSelectedProducts(selectedProducts: List<Product>) {
        val mapToViewModelSelectedProduct = selectedProducts.map { product ->
            ProductCheckout(
                product = product,
                quantity = 1,
            )
        }
        _checkoutState.value = _checkoutState.value.copy(
            selectedProducts = mapToViewModelSelectedProduct,
            subTotal = mapToViewModelSelectedProduct.sumOf { product -> product.quantity * product.product.price }
                .toLong()
        )
    }
}