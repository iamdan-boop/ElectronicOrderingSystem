package com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel

import com.sti.sticanteen.data.network.entity.Product

sealed class CheckoutEvent {
    data class SetSelectedProducts(
        val selectedProducts: List<Product>
    ) : CheckoutEvent()

    data class OnIncrementProductQuantity(
        val productCheckout: ProductCheckout
    ) : CheckoutEvent()

    data class OnDecrementProductQuantity(
        val productCheckout: ProductCheckout
    ) : CheckoutEvent()

    object CheckoutProducts : CheckoutEvent()
}