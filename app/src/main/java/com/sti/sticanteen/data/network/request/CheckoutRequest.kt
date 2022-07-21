package com.sti.sticanteen.data.network.request


data class CheckoutRequest(
    val products: List<ProductRequest> = emptyList()
)