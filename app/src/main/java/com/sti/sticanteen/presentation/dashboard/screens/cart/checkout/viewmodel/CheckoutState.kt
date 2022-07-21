package com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel

import com.sti.sticanteen.data.network.entity.Product

data class CheckoutState(
    val selectedProducts: List<ProductCheckout> = emptyList(),
    val isLoading: Boolean = false,
    val referenceNumber: String = "",
    val subTotal: Long = 0L,
)


data class ProductCheckout(
    val product: Product,
    val quantity: Int
)