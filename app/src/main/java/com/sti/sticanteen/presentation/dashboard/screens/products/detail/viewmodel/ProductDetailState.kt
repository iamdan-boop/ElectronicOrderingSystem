package com.sti.sticanteen.presentation.dashboard.screens.products.detail.viewmodel

data class ProductDetailState(
    val quantity: Int = 0,
    val isLoading: Boolean = false,
    val success: Boolean = false,
)