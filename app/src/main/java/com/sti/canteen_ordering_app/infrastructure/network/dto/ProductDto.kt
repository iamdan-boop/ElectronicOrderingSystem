package com.sti.canteen_ordering_app.infrastructure.network.dto

import java.math.BigInteger

class ProductDto(
    val id: Long,
    val productName: String,
    val price: BigInteger,
    val imageUrl: String,
)