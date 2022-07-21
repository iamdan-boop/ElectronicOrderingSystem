package com.sti.sticanteen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val productId: Long,
    val productName: String,
    val price: Double,
    val type: Int,
    val imageUrl: String,
)