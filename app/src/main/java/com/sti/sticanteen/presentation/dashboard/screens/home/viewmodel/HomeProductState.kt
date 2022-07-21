package com.sti.sticanteen.presentation.dashboard.screens.home.viewmodel

import com.sti.sticanteen.data.network.entity.Product

data class HomeProductState(
    val products: Map<String, List<Product>> = mapOf(),
)