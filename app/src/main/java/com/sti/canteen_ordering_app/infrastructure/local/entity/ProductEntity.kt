package com.sti.canteen_ordering_app.infrastructure.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long? = null,
    val productName: String,
    val price: BigInteger,
    val imageUrl: String,
)