package com.sti.sticanteen.data.network.entity

data class Cart(
    val id: Long,
    val products: List<Product> = emptyList(),
)