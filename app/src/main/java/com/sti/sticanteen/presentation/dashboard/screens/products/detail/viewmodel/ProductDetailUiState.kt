package com.sti.sticanteen.presentation.dashboard.screens.products.detail.viewmodel

sealed class ProductDetailUiState {
    data class Error(val message: String) : ProductDetailUiState()
    object Loading : ProductDetailUiState()
    object Success : ProductDetailUiState()
}
