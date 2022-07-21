package com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.viewmodel

import com.sti.sticanteen.data.network.entity.Product

data class CartListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val selectedProducts: List<Product> = emptyList()
)