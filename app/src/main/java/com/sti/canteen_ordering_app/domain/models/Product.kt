package com.sti.canteen_ordering_app.domain.models

import java.math.BigInteger

data class Product(
    val id: Long,
    val productName: String,
    val price: BigInteger,
    val imageUrl: String,
)