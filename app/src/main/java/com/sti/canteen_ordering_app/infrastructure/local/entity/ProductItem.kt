package com.sti.canteen_ordering_app.infrastructure.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "product_items", foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("productId")
        )
    ]
)
data class ProductItem(
    @PrimaryKey val id: Long? = null,
    val productId: Long,
    val quantity: Int,
)