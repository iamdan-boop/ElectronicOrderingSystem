package com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.viewmodel

import com.sti.sticanteen.data.network.entity.Product

sealed class CartListEvent {
    data class ProductClick(val product: Product) : CartListEvent()
    data class ProductLongClick(val product: Product) : CartListEvent()
    object ClearSelectedProducts : CartListEvent()
    object SelectAllProducts : CartListEvent()
}